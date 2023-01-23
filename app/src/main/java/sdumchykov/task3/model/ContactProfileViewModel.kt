package sdumchykov.task3.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactProfileViewModel : ViewModel() {
    private val _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact> = _contact

    fun pickContact(contact:Contact){
        _contact.value = contact
    }

}