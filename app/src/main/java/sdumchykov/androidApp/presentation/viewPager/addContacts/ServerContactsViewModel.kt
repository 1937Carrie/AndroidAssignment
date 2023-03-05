package sdumchykov.androidApp.presentation.viewPager.addContacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sdumchykov.androidApp.data.repository.ServerApiRepositoryImpl
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ServerContactsViewModel @Inject constructor(
    private val sharedPreferencesStorage: Storage,
    private val serverApi: ServerApiRepositoryImpl
) : ViewModel() {

    private val _allUsersLiveData = MutableLiveData<List<User>>(listOf())
    val allUsersLiveData: LiveData<List<User>> = _allUsersLiveData

    init {
        getAllUsers()
    }

    private fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                serverApi.getUsers(Constants.BEARER_TOKEN + getAccessToken())
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                response.body()?.let {
                    _allUsersLiveData.postValue(it.data.users)
                }
            }
        }
    }


    private fun getAccessToken(): String {
        return sharedPreferencesStorage.getString(Constants.ACCESS_TOKEN) ?: ""
    }
}