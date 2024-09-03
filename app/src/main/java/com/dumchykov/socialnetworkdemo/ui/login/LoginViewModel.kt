package com.dumchykov.socialnetworkdemo.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val dataStoreProvider: DataStoreProvider,
) : ViewModel() {
    private val _loginState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val loginState get() = _loginState.asStateFlow()

    private val _credentialsState = MutableStateFlow("" to "")
    val credentialsState get() = _credentialsState.asStateFlow()

    private fun updateState(reducer: ResponseState.() -> ResponseState) {
        _loginState.update(reducer)
    }

    init {
        checkCachedCredentials()
    }

    private fun checkCachedCredentials() {
        viewModelScope.launch {
            val (email, password) = dataStoreProvider.readCredentials().first()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                _credentialsState.update { email to password }
            }
        }
    }

    fun authorize(email: String, password: String) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val authorizeResponse = contactRepository.authorize(email, password)
            updateState { authorizeResponse }
        }
    }

    fun saveCredentials(email: String, password: String): Job {
        return viewModelScope.launch {
            dataStoreProvider.writeCredentials(email, password)
        }
    }
}