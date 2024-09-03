package com.dumchykov.socialnetworkdemo.ui.addcontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class AddContactsViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
) : ViewModel() {
    private val _addContactsState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val addContactsState get() = _addContactsState.asStateFlow()

    fun updateState(reducer: ResponseState.() -> ResponseState) {
        _addContactsState.update(reducer)
    }

    fun getAllUsers(bearerToken: String) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val getUsersResponse = contactRepository.getUsers(bearerToken)
            updateState { getUsersResponse }
        }
    }

    fun addContact(bearerToken: String, userId: Int, contactId: ContactId) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val addContactResponse = contactRepository.addContact(bearerToken, userId, contactId)
            updateState { addContactResponse }
        }
    }

    fun isUserAddedToContact(userContactIdList: List<Int>, userId: Int): Boolean {
        return userContactIdList.contains(userId)
    }
}