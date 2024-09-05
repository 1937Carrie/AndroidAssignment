package com.dumchykov.socialnetworkdemo.ui.screens.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.room.ContactsDatabase
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.logic.toAuthorizedUserDBO
import com.dumchykov.socialnetworkdemo.domain.logic.toContact
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ApiContact
import com.dumchykov.socialnetworkdemo.domain.webapi.models.EditUserResponse
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
class EditProfileViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val database: ContactsDatabase,
) : ViewModel() {
    private val _editProfileState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val editProfileState get() = _editProfileState.asStateFlow()

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

    private fun updateState(reducer: ResponseState.() -> ResponseState) {
        _editProfileState.update(reducer)
    }

    fun editProfile(userId: Int, bearerToken: String, apiContact: ApiContact) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val editUserResponse = contactRepository.editUser(userId, bearerToken, apiContact)
            if (editUserResponse is ResponseState.Success<*>) {
                val (user) = editUserResponse.data as EditUserResponse
                withContext(Dispatchers.IO) {
                    database.contactsDao.insertCurrentUser(user.toAuthorizedUserDBO())
                }
            }
            updateState { editUserResponse }
        }
    }
}