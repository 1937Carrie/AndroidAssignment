package com.dumchykov.socialnetworkdemo.ui.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import com.dumchykov.socialnetworkdemo.domain.webapi.models.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
) : ViewModel() {
    private val _editProfileState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val editProfileState get() = _editProfileState.asStateFlow()

    private fun updateState(reducer: ResponseState.() -> ResponseState) {
        _editProfileState.update(reducer)
    }

    fun editProfile(userId: Int, bearerToken: String, contact: Contact) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val editUserResponse = contactRepository.editUser(userId, bearerToken, contact)
            updateState { editUserResponse }
        }
    }
}