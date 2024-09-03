package com.dumchykov.socialnetworkdemo.ui

import androidx.lifecycle.ViewModel
import com.dumchykov.socialnetworkdemo.domain.webapi.models.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _shareState = MutableStateFlow(ShareState())
    val shareState get() = _shareState.asStateFlow()

    fun updateState(reducer: ShareState.() -> ShareState) {
        _shareState.update(reducer)
    }
}

data class ShareState(
    val currentUser: Contact = Contact(
        id = 0,
        name = null,
        email = null,
        phone = null,
        career = null,
        address = null,
        birthday = null,
        facebook = null,
        instagram = null,
        twitter = null,
        linkedin = null,
        image = null,
        created_at = "",
        updated_at = ""
    ),
    val accessToken: String = "",
    val refreshToken: String = "",
    val userList: List<Contact> = emptyList(),
    val userContactIdList: List<Int> = emptyList(),
)