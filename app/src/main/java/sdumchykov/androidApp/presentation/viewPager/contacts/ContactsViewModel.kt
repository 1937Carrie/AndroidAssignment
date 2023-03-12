package sdumchykov.androidApp.presentation.viewPager.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sdumchykov.androidApp.R
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.domain.model.requestModels.ContactIdModel
import sdumchykov.androidApp.domain.repository.LocalUsersRepository
import sdumchykov.androidApp.domain.repository.NetworkUsersRepository
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.domain.utils.Response
import sdumchykov.androidApp.domain.utils.Status
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val db: AppDatabase,
    private val sharedPreferencesStorage: Storage,
    private val localUsersRepository: LocalUsersRepository,
    private val serverApi: NetworkUsersRepository
) : ViewModel() {

    private val _userContacts = MutableLiveData<List<User>>(listOf())
    val userContacts: LiveData<List<User>> = _userContacts

    private val _statusUserContacts = MutableLiveData<Response<Status>>()
    val statusUserContacts: LiveData<Response<Status>> = _statusUserContacts

    val selectedEvent = MutableLiveData(false)

    fun initRealUsersList() {
        viewModelScope.launch(Dispatchers.IO) {
            _userContacts.postValue(localUsersRepository.getRealUsers())
        }
    }

    fun apiGetUserContacts() {
        viewModelScope.launch(Dispatchers.IO) {
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
                setSuccessStatus()
            } else {
                setErrorStatus(R.string.messageUnexpectedState)
            }
        }
    }

    fun apiAddContact(contactId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun apiDeleteContact(contactId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoadingStatus()
            val userDao = db.userDao()
            val userId = userDao.getUser().id

            try {
                serverApi.deleteContact(
                    userId,
                    contactId,
                    Constants.BEARER_TOKEN + getAccessToken()
                )
            } catch (e: IOException) {
                setErrorStatus(R.string.messageIOException)
                return@launch
            } catch (e: HttpException) {
                setErrorStatus(R.string.messageHTTPException)
                return@launch
            }
            setSuccessStatus()
            apiGetUserContacts()
        }
    }

    fun getContactByPosition(position: Int): User? {
        return userContacts.value?.get(position)
    }

    private fun setSuccessStatus() {
        _statusUserContacts.postValue(Response.success(Status.SUCCESS))
    }

    private fun setLoadingStatus() {
        _statusUserContacts.postValue(Response.loading(null))
    }

    private fun setErrorStatus(messageResourceId: Int) {
        _statusUserContacts.postValue(Response.error(messageResourceId, null))
    }

}
