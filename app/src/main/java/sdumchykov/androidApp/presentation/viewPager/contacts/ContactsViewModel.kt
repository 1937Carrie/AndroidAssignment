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
import sdumchykov.androidApp.R
import sdumchykov.androidApp.data.repository.NetworkUsersRepositoryImpl
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.domain.model.requestModels.ContactIdModel
import sdumchykov.androidApp.domain.model.requestModels.EditProfileUser
import sdumchykov.androidApp.domain.repository.LocalUsersRepository
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.domain.utils.Response
import sdumchykov.androidApp.domain.utils.Status
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val sharedPreferencesStorage: Storage,
    private val localUsersRepository: LocalUsersRepository,
    private val serverApi: NetworkUsersRepositoryImpl,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _userContacts = MutableLiveData<List<User>>(listOf())
    val userContacts: LiveData<List<User>> = _userContacts

    private val _statusUserContacts = MutableLiveData<Response<Status>>()
    val statusUserContacts: LiveData<Response<Status>> = _statusUserContacts

    val selectedEvent = MutableLiveData(false)

    fun initHardcodedDataList() {
        viewModelScope.launch(Dispatchers.IO) {
            _userContacts.postValue(localUsersRepository.getHardcodedUsers())
        }
    }

    fun initRealUsersList() {
        viewModelScope.launch(Dispatchers.IO) {
            _userContacts.postValue(localUsersRepository.getRealUsers())
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

            setLoadingStatus()

            val response = try {
                serverApi.getAccountUsers(userId, Constants.BEARER_TOKEN + getAccessToken())
            } catch (e: IOException) {
                setErrorStatus(R.string.messageIOException)
                return@launch
            } catch (e: HttpException) {
                setErrorStatus(R.string.messageHTTPException)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                response.body()?.let {
                    _userContacts.postValue(it.data.contacts)
                }
                setSuccessStatus(Status.SUCCESS)
            } else {
                setErrorStatus(R.string.messageUnexpectedState)
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
        _userContacts.value = userContacts.value?.toMutableList()?.apply {
            add(index, contact)
        }
    }

    fun removeItem(contact: User?) {
        _userContacts.value = userContacts.value?.toMutableList()?.apply { remove(contact) }
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
        _userContacts.value = userContacts.value?.toMutableList()?.apply {
            add(
                User(
                    id = userContacts.value?.size ?: 0,
                    name = name,
                    career = profession,
                    image = Constants.HARDCODED_IMAGE_URL
                )
            )
        }
    }

    fun getContactByPosition(position: Int): User? {
        return userContacts.value?.get(position)
    }

    private fun setSuccessStatus(status: Status) {
        _statusUserContacts.postValue(Response.success(status))
    }

    private fun setLoadingStatus() {
        _statusUserContacts.postValue(Response.loading(null))
    }

    private fun setErrorStatus(messageResourceId: Int) {
        _statusUserContacts.postValue(Response.error(messageResourceId, null))
    }

}
