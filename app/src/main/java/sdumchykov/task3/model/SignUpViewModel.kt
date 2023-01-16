package sdumchykov.task3.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun set(email: String) {
        this.email.postValue(email)
    }

    fun get(): String {
        return email.value.toString()
    }
}