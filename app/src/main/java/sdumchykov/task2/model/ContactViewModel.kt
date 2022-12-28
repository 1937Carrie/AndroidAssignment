package sdumchykov.task2.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sdumchykov.task2.data.Datasource

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val _userLiveData = MutableLiveData<List<Contact>>()
    val userListLiveData: LiveData<List<Contact>> = _userLiveData

    init {
        _userLiveData.value = Datasource().contactList()
    }

    val liveData = Datasource().contactList()


}