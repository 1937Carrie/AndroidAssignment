package com.dumchykov.socialnetworkdemo.ui.screens.details

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
import com.dumchykov.socialnetworkdemo.domain.webapi.models.Contact as ApiContact

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
) : ViewModel() {
    private val _detailsState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val detailsState get() = _detailsState.asStateFlow()

    private val _contactState = MutableStateFlow(Contact())
    val contactState get() = _contactState.asStateFlow()

    private fun updateState(reducer: ResponseState.() -> ResponseState) {
        _detailsState.update(reducer)
    }

    fun updateContactState(reducer: Contact.() -> Contact) {
        _contactState.update(reducer)
    }

    fun getUserById(users: List<ApiContact>, userContactIdList: List<Int>, userId: Int) {
        val contactApi = users.first { it.id == userId }
        val contact = Contact(
            id = contactApi.id,
            name = contactApi.name.toString(),
            career = contactApi.career.toString(),
            address = contactApi.address.toString(),
            isSelected = false,
            isAdded = userContactIdList.contains(userId),
        )
        updateContactState { contact }
    }

    fun addContact(bearerToken: String, userId: Int, contactId: ContactId) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val addContactResponse = contactRepository.addContact(bearerToken, userId, contactId)
            updateState { addContactResponse }
        }
    }

    fun isUserAddedToContact(userContactIdList: List<Int>): Boolean {
        return userContactIdList.contains(contactState.value.id)
    }
}