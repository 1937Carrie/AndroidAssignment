package com.dumchykov.socialnetworkdemo.ui.screens.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val dataStore: DataStoreProvider,
) : ViewModel() {
    private val _exitFlag = MutableStateFlow(false)
    val exitFlag get() = _exitFlag.asStateFlow()

    private val _name = MutableStateFlow("")
    val name get() = _name.asStateFlow()

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