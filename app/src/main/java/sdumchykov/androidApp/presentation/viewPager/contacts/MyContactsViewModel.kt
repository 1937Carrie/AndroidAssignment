package sdumchykov.androidApp.presentation.viewPager.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.domain.repository.UsersRepository
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import javax.inject.Inject

@HiltViewModel
class MyContactsViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val storage: Storage
) : ViewModel() {

    private val _userLiveData = MutableLiveData<List<UserModel>>(listOf())
    val userLiveData: LiveData<List<UserModel>> = _userLiveData

    val selectedEvent = MutableLiveData(false)
//    val loadEvent = MutableLiveData<Results>()

    init {
        if (getFetchContactList()) initRealUsersList() else initHardcodedDataList()
        // 1. Create a live data or flow
        // 2. Get FETCH_CONTACT_LIST_KEY
        // 3. Store it in the created live data or flow
        // 4. observe the live data or collect the flow in your fragments
    }

    fun initHardcodedDataList() {
        viewModelScope.launch(Dispatchers.IO) {
            _userLiveData.postValue(usersRepository.getHardcodedUsers())
        }
    }

    fun initRealUsersList() {
        viewModelScope.launch(Dispatchers.IO) {
            _userLiveData.postValue(usersRepository.getRealUsers())
        }
    }

    internal fun getFetchContactList() = storage.getBoolean(Constants.FETCH_CONTACT_LIST_KEY)

    fun addItem(contact: UserModel, index: Int) {
        _userLiveData.value = userLiveData.value?.toMutableList()?.apply {
            add(index, contact)
        }
    }

    fun removeItem(contact: UserModel?) {
        _userLiveData.value = userLiveData.value?.toMutableList()?.apply { remove(contact) }
    }

    fun addNewItem(name: String, profession: String) {
        _userLiveData.value = userLiveData.value?.toMutableList()?.apply {
            add(
                UserModel(
                    id = userLiveData.value?.size ?: 0,
                    name = name,
                    profession = profession,
                    photo = Constants.HARDCODED_IMAGE_URL
                )
            )
        }
    }

    fun getContactByPosition(position: Int): UserModel? {
        return userLiveData.value?.get(position)
    }
}
