package com.dumchykov.socialnetworkdemo.ui.screens.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import com.dumchykov.socialnetworkdemo.data.room.ContactsDatabase
import com.dumchykov.socialnetworkdemo.domain.logic.toContact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val database: ContactsDatabase,
    private val dataStore: DataStoreProvider,
) : ViewModel() {
    private val _exitFlag = MutableStateFlow(false)
    val exitFlag get() = _exitFlag.asStateFlow()

    private val _authorizedUser = MutableStateFlow(Contact(id = -1))
    val authorizedUser get() = _authorizedUser.asStateFlow()

    init {
        getAuthorizedUser()
    }

    private fun updateState(reducer: Contact.() -> Contact) {
        _authorizedUser.update(reducer)
    }

    private fun getAuthorizedUser() {
        viewModelScope.launch {
            val authUser = withContext(Dispatchers.IO) {
                database.contactsDao.getCurrentUser()
            }
            updateState { authUser.toContact() }
        }
    }

    fun clearCredentials() {
        viewModelScope.launch {
            async { dataStore.clearCredentials() }.await()
            _exitFlag.update { true }
        }
    }
}