package com.dumchykov.socialnetworkdemo.ui.mycontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ContactId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyContactsViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
) : ViewModel() {
    private val _myContactsState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val myContactsState get() = _myContactsState.asStateFlow()

    private val _contactListState = MutableStateFlow(MyContactsState())
    val contactListState get() = _contactListState.asStateFlow()

    fun updateState(reducer: ResponseState.() -> ResponseState) {
        _myContactsState.update(reducer)
    }

    fun updateContactListState(reducer: MyContactsState.() -> MyContactsState) {
        _contactListState.update(reducer)
    }

    private fun isMultiSelected(): Boolean {
        return contactListState.value.contacts.firstOrNull { it.isSelected } != null
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
        val contactMutableList = contactListState.value.contacts.toMutableList()
        val newContact = Contact(
            id = contactMutableList.size,
            name = name,
            career = career,
            address = address
        )
        contactMutableList.add(newContact)
        contactMutableList.sortBy { it.id }
        updateContactListState { copy(contacts = contactMutableList) }
    }

    fun updateContactCheckState(contact: Contact) {
        val contactMutableList = contactListState.value.contacts.toMutableList()
        val indexOfFirst = contactMutableList.indexOfFirst { it.id == contact.id }
        contactMutableList[indexOfFirst] = contact.copy(isSelected = contact.isSelected.not())
        updateContactListState { copy(contacts = contactMutableList) }
        val isMultiselect = isMultiSelected()
        updateContactListState { copy(isMultiselect = isMultiselect) }
    }

    fun multipleRemovingContact(userId: Int, bearerToken: String) {
        val selectedContacts =
            contactListState.value.contacts.toMutableList().filter { it.isSelected }
        selectedContacts.forEach { contact ->
            removeContact(userId, contact.id, bearerToken)
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
            updateState { getUsersResponse }
        }
    }
}