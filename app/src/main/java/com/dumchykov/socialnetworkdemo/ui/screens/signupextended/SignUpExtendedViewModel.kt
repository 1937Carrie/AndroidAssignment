package com.dumchykov.socialnetworkdemo.ui.screens.signupextended

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.room.ContactsDatabase
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.logic.toContact
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ApiContact
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
class SignUpExtendedViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val database: ContactsDatabase,
) : ViewModel() {
    private val _signUpExtendedState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val signUpExtendedState get() = _signUpExtendedState.asStateFlow()

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
        _signUpExtendedState.update(reducer)
    }

    fun editUser(userId: Int, bearerToken: String, user: ApiContact) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val editUserResponse = contactRepository.editUser(userId, bearerToken, user)
            updateState { editUserResponse }
        }
    }
}