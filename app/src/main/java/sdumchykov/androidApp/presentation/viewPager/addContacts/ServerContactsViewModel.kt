package sdumchykov.androidApp.presentation.viewPager.addContacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sdumchykov.androidApp.R
import sdumchykov.androidApp.data.repository.NetworkUsersRepositoryImpl
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.domain.utils.Response
import sdumchykov.androidApp.domain.utils.Status
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ServerContactsViewModel @Inject constructor(
    private val sharedPreferencesStorage: Storage,
    private val serverApi: NetworkUsersRepositoryImpl
) : ViewModel() {

    private val _allUsers = MutableLiveData<List<User>>(listOf())
    val allUsers: LiveData<List<User>> = _allUsers

    private val _statusAllUsers = MutableLiveData<Response<Status>>()
    val statusAllUsers: LiveData<Response<Status>> = _statusAllUsers

    init {
        getAllUsers()
    }

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            setLoadingStatus()

            val response = try {
                serverApi.getUsers(Constants.BEARER_TOKEN + getAccessToken())
            } catch (e: IOException) {
                setErrorStatus(R.string.messageIOException)
                return@launch
            } catch (e: HttpException) {
                setErrorStatus(R.string.messageHTTPException)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                response.body()?.let {
                    _allUsers.postValue(it.data.users)
                }
                setSuccessStatus(Status.SUCCESS)
            } else {
                setErrorStatus(R.string.messageUnexpectedState)

            }
        }
    }

    private fun getAccessToken(): String {
        return sharedPreferencesStorage.getString(Constants.ACCESS_TOKEN) ?: ""
    }

    private fun setSuccessStatus(status: Status) {
        _statusAllUsers.postValue(Response.success(status))
    }

    private fun setLoadingStatus() {
        _statusAllUsers.postValue(Response.loading(null))
    }

    private fun setErrorStatus(messageResourceId: Int) {
        _statusAllUsers.postValue(Response.error(messageResourceId, null))
    }
}