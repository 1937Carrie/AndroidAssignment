package com.dumchykov.socialnetworkdemo.ui.screens.mycontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.contactsprovider.IndicatorContact
import com.dumchykov.socialnetworkdemo.data.room.ContactsDatabase
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.logic.toContact
import com.dumchykov.socialnetworkdemo.domain.logic.toContactDBO
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ContactId
import com.dumchykov.socialnetworkdemo.domain.webapi.models.MultipleUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.dumchykov.socialnetworkdemo.ui.screens.myprofile.Contact as PlainContact

@HiltViewModel
class MyContactsViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val database: ContactsDatabase,
) : ViewModel() {
    private val _myContactsState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val myContactsState get() = _myContactsState.asStateFlow()

    private val _contactListState = MutableStateFlow(MyContactsState())
    val contactListState get() = _contactListState.asStateFlow()

    private val _processingIndicatorContact = MutableStateFlow(IndicatorContact())
    val processingContact get() = _processingIndicatorContact.asStateFlow()

    private val _authorizedUser = MutableStateFlow(PlainContact(id = -1))
    val authorizedUser get() = _authorizedUser.asStateFlow()

    init {
        getAuthorizedUser()
    }

    private fun getAuthorizedUser() {
        viewModelScope.launch {
            val authUser = withContext(Dispatchers.IO) {
                database.contactsDao.getCurrentUser()
            }
            _authorizedUser.update { authUser.toContact() }
        }
    }

    fun updateState(reducer: ResponseState.() -> ResponseState) {
        _myContactsState.update(reducer)
    }

    fun updateContactListState(reducer: MyContactsState.() -> MyContactsState) {
        _contactListState.update(reducer)
    }

    private fun isMultiSelected(): Boolean {
        return contactListState.value.indicatorContacts.firstOrNull { it.isSelected } != null
    }

    fun removeContact(userId: Int, contactId: Int, bearerToken: String) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val deleteContactResponse =
                contactRepository.deleteContact(userId, contactId, bearerToken)
            updateState { deleteContactResponse }
        }
    }

    fun addContact(bearerToken: String, userId: Int, contactId: ContactId) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val addContactResponse = contactRepository.addContact(bearerToken, userId, contactId)
            updateState { addContactResponse }
        }
    }

    fun addContact(name: String, career: String, address: String) {
        val contactMutableList = contactListState.value.indicatorContacts.toMutableList()
        val newIndicatorContact = IndicatorContact(
            id = contactMutableList.size,
            name = name,
            career = career,
            address = address
        )
        contactMutableList.add(newIndicatorContact)
        contactMutableList.sortBy { it.id }
        updateContactListState { copy(indicatorContacts = contactMutableList) }
    }

    fun updateContactCheckState(indicatorContact: IndicatorContact) {
        val contactMutableList = contactListState.value.indicatorContacts.toMutableList()
        val indexOfFirst = contactMutableList.indexOfFirst { it.id == indicatorContact.id }
        contactMutableList[indexOfFirst] =
            indicatorContact.copy(isSelected = indicatorContact.isSelected.not())
        updateContactListState { copy(indicatorContacts = contactMutableList) }
        val isMultiselect = isMultiSelected()
        updateContactListState { copy(isMultiselect = isMultiselect) }
    }

    fun multipleRemovingContact(userId: Int, bearerToken: String) {
        val selectedContacts =
            contactListState.value.indicatorContacts.toMutableList().filter { it.isSelected }
        selectedContacts.forEach { indicatorContact ->
            removeContact(userId, indicatorContact.id, bearerToken)
        }
        updateContactListState { copy(isMultiselect = false) }
    }

    fun getUserContacts(userId: Int, bearerToken: String) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val contactUsersResponse = contactRepository.getUserContacts(userId, bearerToken)
            updateState { contactUsersResponse }
        }
    }

    fun getAllUsers(bearerToken: String) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val getUsersResponse = contactRepository.getUsers(bearerToken)
            if (getUsersResponse is ResponseState.Success<*>) {
                getUsersResponse.data as MultipleUserResponse
                withContext(Dispatchers.IO) {
                    database.contactsDao.insertAll(getUsersResponse.data.users.map { it.toContactDBO() })
                }
            }
            updateState { getUsersResponse }
        }
    }

    fun setProcessingContact(indicatorContact: IndicatorContact) {
        _processingIndicatorContact.update { indicatorContact }
    }
}