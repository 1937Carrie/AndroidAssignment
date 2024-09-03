package com.dumchykov.socialnetworkdemo.ui.signupextended

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
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
class SignUpExtendedViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
) : ViewModel() {
    private val _signUpExtendedState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val signUpExtendedState get() = _signUpExtendedState.asStateFlow()

    private fun updateState(reducer: ResponseState.() -> ResponseState) {
        _signUpExtendedState.update(reducer)
    }

    fun editUser(userId: Int, bearerToken:String, user: Contact) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val editUserResponse = contactRepository.editUser(userId, bearerToken, user)
            updateState { editUserResponse }
        }
    }
}