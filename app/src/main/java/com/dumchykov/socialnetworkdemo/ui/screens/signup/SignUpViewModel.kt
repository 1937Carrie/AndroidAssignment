package com.dumchykov.socialnetworkdemo.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val dataStore: DataStoreProvider,
) : ViewModel() {
    private val _signUpState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val signUpState get() = _signUpState.asStateFlow()

    private fun updateState(reducer: ResponseState.() -> ResponseState) {
        _signUpState.update(reducer)
    }

    fun saveCredentials(email: String, password: String): Job {
        return viewModelScope.launch {
            dataStore.writeCredentials(email, password)
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val registerResponse = contactRepository.register(email, password)
            updateState { registerResponse }
        }
    }
}