package com.dumchykov.socialnetworkdemo.ui.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyProfileViewModel(
    private val dataStore: DataStoreProvider,
) : ViewModel() {
    private val _exitFlag = MutableStateFlow(false)
    val exitFlag get() = _exitFlag.asStateFlow()

    private val _name = MutableStateFlow("")
    val name get() = _name.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.readEmail().collect { email ->
                _name.update { email }
            }
        }
    }

    fun clearCredentials() {
        viewModelScope.launch {
            async { dataStore.clearCredentials() }.await()
            _exitFlag.update { true }
        }
    }

    companion object {
        fun factory(dataStore: DataStoreProvider): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    MyProfileViewModel(dataStore)
                }
            }
        }
    }
}