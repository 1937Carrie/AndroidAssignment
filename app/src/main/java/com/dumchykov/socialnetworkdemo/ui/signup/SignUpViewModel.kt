package com.dumchykov.socialnetworkdemo.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val dataStore: DataStoreProvider,
) : ViewModel() {
    private val _credentials = MutableStateFlow(Pair("", ""))
    val credentials get() = _credentials.asStateFlow()

    private val _navFlag = MutableStateFlow(false)
    val navFlag get() = _navFlag.asStateFlow()

    init {
        viewModelScope.launch {
            val (email, password) = dataStore.readCredentials().first()
            if (email.isEmpty() || password.isEmpty()) return@launch
            _credentials.update { email to password }
            _navFlag.update { true }
        }
    }

    fun saveCredentials(email: String, password: String) {
        viewModelScope.launch {
            dataStore.writeCredentials(email, password)
            _navFlag.update { true }
        }
    }

    companion object {
        fun factory(dataStore: DataStoreProvider): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SignUpViewModel(dataStore)
                }
            }
        }
    }
}