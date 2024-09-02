package com.dumchykov.socialnetworkdemo.ui.mycontacts

import androidx.lifecycle.ViewModel
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.data.contactsprovider.getHardcodedContacts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyContactsViewModel : ViewModel() {
    private val _myContactsState = MutableStateFlow(MyContactsState())
    val myContactsState get() = _myContactsState.asStateFlow()

    init {
        updateContactsState { copy(contacts = getHardcodedContacts().toMutableList()) }
    }

    private fun updateContactsState(reducer: MyContactsState.() -> MyContactsState) {
        _myContactsState.update(reducer)
    }

    private fun isMultiSelected(): Boolean {
        return myContactsState.value.contacts.firstOrNull { it.isSelected } != null
    }

    fun removeContact(contactId: Int) {
        val contactMutableList = myContactsState.value.contacts.toMutableList()
        contactMutableList.removeIf { it.id == contactId }
        updateContactsState { copy(contacts = contactMutableList) }
    }

    fun addContact(contact: Contact) {
        val contactMutableList = myContactsState.value.contacts.toMutableList()
        contactMutableList.add(contact)
        contactMutableList.sortBy { it.id }
        updateContactsState { copy(contacts = contactMutableList) }
    }

    fun addContact(name: String, career: String, address: String) {
        val contactMutableList = myContactsState.value.contacts.toMutableList()
        val newContact = Contact(
            id = contactMutableList.size,
            name = name,
            career = career,
            address = address
        )
        contactMutableList.add(newContact)
        contactMutableList.sortBy { it.id }
        updateContactsState { copy(contacts = contactMutableList) }
    }

    fun updateContactCheckState(contact: Contact) {
        val contactMutableList = myContactsState.value.contacts.toMutableList()
        val indexOfFirst = contactMutableList.indexOfFirst { it.id == contact.id }
        contactMutableList[indexOfFirst] = contact.copy(isSelected = contact.isSelected.not())
        updateContactsState { copy(contacts = contactMutableList) }
        val isMultiselect = isMultiSelected()
        updateContactsState { copy(isMultiselect = isMultiselect) }
    }

    fun multipleRemovingContact() {
        val selectedContacts =
            myContactsState.value.contacts.toMutableList().filter { it.isSelected }
        selectedContacts.forEach { contact ->
            removeContact(contact.id)
        }
        updateContactsState { copy(isMultiselect = false) }
    }
}