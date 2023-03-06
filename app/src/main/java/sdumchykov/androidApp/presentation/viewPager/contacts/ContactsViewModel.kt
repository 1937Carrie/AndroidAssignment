package sdumchykov.androidApp.presentation.viewPager.contacts

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sdumchykov.androidApp.data.repository.ServerApiRepositoryImpl
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.domain.model.requestModels.ContactIdModel
import sdumchykov.androidApp.domain.model.requestModels.EditProfileUser
import sdumchykov.androidApp.domain.repository.UsersRepository
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val sharedPreferencesStorage: Storage,
    private val usersRepository: UsersRepository,
    private val serverApi: ServerApiRepositoryImpl,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _userLiveData = MutableLiveData<List<User>>(listOf())
    val userLiveData: LiveData<List<User>> = _userLiveData

    val selectedEvent = MutableLiveData(false)

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

    fun apiGetUserContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "database-name"
            ).allowMainThreadQueries().build()
            val userDao = db.userDao()
            val userId = userDao.getUser().id

            val response = try {
                serverApi.getAccountUsers(userId, Constants.BEARER_TOKEN + getAccessToken())
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                response.body()?.let {
                    _userLiveData.postValue(it.data.contacts)
                }
            }
        }
    }

    fun apiAddContact(contactId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(
                context, AppDatabase::class.java, "database-name"
            ).build()
            val userDao = db.userDao()
            val userId = userDao.getUser().id

            try {
                serverApi.addContact(
                    userId,
                    Constants.BEARER_TOKEN + getAccessToken(),
                    ContactIdModel(contactId)
                )
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }
        }
    }

    private fun getAccessToken(): String {
        return sharedPreferencesStorage.getString(Constants.ACCESS_TOKEN) ?: ""
    }

    fun addItem(contact: User, index: Int) {
        _userLiveData.value = userLiveData.value?.toMutableList()?.apply {
            add(index, contact)
        }
    }

    fun removeItem(contact: User?) {
        _userLiveData.value = userLiveData.value?.toMutableList()?.apply { remove(contact) }
    }

    fun apiDeleteContact(contactId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "database-name"
            ).allowMainThreadQueries().build()
            val userDao = db.userDao()
            val userId = userDao.getUser().id

            try {
                serverApi.deleteContact(
                    userId,
                    contactId,
                    Constants.BEARER_TOKEN + getAccessToken()
                )
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }
        }
    }

    fun apiEditProfile(name: String, phone: String, address: String, career: String, DOB: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(
                context, AppDatabase::class.java, "database-name"
            ).build()
            val userDao = db.userDao()
            val userId = userDao.getUser().id

            try {
                serverApi.editUser(
                    userId,
                    Constants.BEARER_TOKEN + getAccessToken(),
                    EditProfileUser(
                        name = name,
                        phone = phone,
                        address = address,
                        career = career,
                        birthday = DOB
                    )
                )
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }
        }
    }

    fun addNewContactManually(name: String, profession: String) {
        _userLiveData.value = userLiveData.value?.toMutableList()?.apply {
            add(
                User(
                    id = userLiveData.value?.size ?: 0,
                    name = name,
                    career = profession,
                    image = Constants.HARDCODED_IMAGE_URL
                )
            )
        }
    }

    fun getContactByPosition(position: Int): User? {
        return userLiveData.value?.get(position)
    }

}
