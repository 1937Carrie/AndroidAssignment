package sdumchykov.task3.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel(private val repository: ArrayList<Contact>) : ViewModel() {
    val contactList = MutableLiveData<ArrayList<Contact>>()

    init {
        getAllContacts()
    }

    fun getAllContacts() {
        contactList.postValue(repository)
    }

    fun addItem(contact: Contact){
        val tmp = contactList.value
        tmp?.add(contact)
        contactList.value = tmp
    }

    fun updateItem(index: Int, newData: Contact) {
        val tmp = contactList.value
        tmp?.set(index, newData)
        contactList.value = tmp
    }

    fun removeItem(contact: Contact) {
        val tmp = contactList.value
        tmp?.remove(contact)
        contactList.value = tmp
    }
}