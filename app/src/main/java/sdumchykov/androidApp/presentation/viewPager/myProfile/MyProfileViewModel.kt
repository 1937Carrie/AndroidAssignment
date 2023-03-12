package sdumchykov.androidApp.presentation.viewPager.myProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.domain.local.User
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val storage: Storage,
    private val db: AppDatabase
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun getDB() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(db.userDao().getUser())
        }
    }

    fun getFetchContactList(): Boolean {
        return storage.getBoolean(Constants.FETCH_CONTACT_LIST_KEY)
    }

    fun setFetchContactList(state: Boolean) {
        storage.save(Constants.FETCH_CONTACT_LIST_KEY, state)
    }

}
