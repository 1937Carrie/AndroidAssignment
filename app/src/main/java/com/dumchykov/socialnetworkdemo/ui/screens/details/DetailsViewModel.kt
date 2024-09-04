package com.dumchykov.socialnetworkdemo.ui.screens.details

import androidx.lifecycle.ViewModel
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.dumchykov.socialnetworkdemo.domain.webapi.models.Contact as ApiContact

@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {
    fun getUserById(users: List<ApiContact>, userContactIdList: List<Int>, userId: Int): Contact {
        val contactApi = users.first { it.id == userId }
        val contact = Contact(
            id = contactApi.id,
            name = contactApi.name.toString(),
            career = contactApi.career.toString(),
            address = contactApi.address.toString(),
            isSelected = false,
            isAdded = userContactIdList.contains(userId),
        )
        return contact
    }
}