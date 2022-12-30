package sdumchykov.task2.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel(private val repository: ArrayList<Contact>) : ViewModel() {
    val contactList = MutableLiveData<ArrayList<Contact>>()

    fun getAllContacts() {
        contactList.postValue(repository)
    }
}