package com.dumchykov.socialnetworkdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import com.dumchykov.socialnetworkdemo.data.room.ContactsDatabase
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.logic.toAuthorizedUserDBO
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import com.dumchykov.socialnetworkdemo.domain.webapi.models.AuthenticationResponse
import com.dumchykov.socialnetworkdemo.domain.webapi.models.Contact
import com.dumchykov.socialnetworkdemo.domain.webapi.models.MultipleContactResponse
import com.dumchykov.socialnetworkdemo.domain.webapi.models.MultipleUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val dataStoreProvider: DataStoreProvider,
    private val database: ContactsDatabase,
) : ViewModel() {
    private val _shareState = MutableStateFlow(ShareState())
    val shareState get() = _shareState.asStateFlow()

    fun updateState(reducer: ShareState.() -> ShareState) {
        _shareState.update(reducer)
    }

    suspend fun authorize() {
        viewModelScope.launch {
            val (email, password) = dataStoreProvider.readCredentials().first()
            val authorizeResponse = contactRepository.authorize(email, password)
            if ((authorizeResponse is ResponseState.Success<*>).not()) return@launch
            val (user, accessToken, refreshToken) = (authorizeResponse as ResponseState.Success<*>).data as AuthenticationResponse
            withContext(Dispatchers.IO) {
                database.contactsDao.insertCurrentUser(user.toAuthorizedUserDBO())
            }
            updateState {
                copy(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            }
        }.join()
    }

    suspend fun getUserContacts() {
        viewModelScope.launch {
            val userId = withContext(Dispatchers.IO) {
                database.contactsDao.getCurrentUser().id
            }
            val bearerToken = shareState.value.accessToken
            val contactUsersResponse = contactRepository.getUserContacts(userId, bearerToken)
            if ((contactUsersResponse is ResponseState.Success<*>).not()) return@launch
            val (contacts) = (contactUsersResponse as ResponseState.Success<*>).data as MultipleContactResponse
            updateState { copy(userContactIdList = contacts.map { it.id }) }
        }.join()
    }

    suspend fun getUsers() {
        viewModelScope.launch {
            val bearerToken = shareState.value.accessToken
            val usersResponse = contactRepository.getUsers(bearerToken)
            if ((usersResponse is ResponseState.Success<*>).not()) return@launch
            val (users) = (usersResponse as ResponseState.Success<*>).data as MultipleUserResponse
            updateState { copy(userList = users) }
        }.join()
    }
}

data class ShareState(
    val accessToken: String = "",
    val refreshToken: String = "",
    val userList: List<Contact> = emptyList(),
    val userContactIdList: List<Int> = emptyList(),
)