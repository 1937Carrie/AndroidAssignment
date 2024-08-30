package com.dumchykov.socialnetworkdemo.ui.mycontacts

import androidx.lifecycle.ViewModel
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.data.contactsprovider.getHardcodedContacts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyContactsViewModel : ViewModel() {
    private val _contacts = MutableStateFlow(mutableListOf<Contact>())
    val contacts get() = _contacts.asStateFlow()

    init {
        updateContactsState { getHardcodedContacts().toMutableList() }
    }

    private fun updateContactsState(reducer: MutableList<Contact>.() -> MutableList<Contact>) {
        _contacts.update(reducer)
    }

    fun removeContact(contactId: Int) {
        val contactMutableList = contacts.value.toMutableList()
        contactMutableList.removeIf { it.id == contactId }
        updateContactsState { contactMutableList }
    }
}