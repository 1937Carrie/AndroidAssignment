package com.dumchykov.socialnetworkdemo.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val dataStore: DataStoreProvider,
) : ViewModel() {
    private val _signUpState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val signUpState get() = _signUpState.asStateFlow()

    fun saveCredentials(email: String, password: String): Job {
        return viewModelScope.launch {
            dataStore.writeCredentials(email, password)
        }
    }
}