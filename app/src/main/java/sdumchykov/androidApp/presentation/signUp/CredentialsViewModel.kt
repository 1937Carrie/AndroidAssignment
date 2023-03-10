package sdumchykov.androidApp.presentation.signUp

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
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.domain.model.requestModels.AuthorizeModel
import sdumchykov.androidApp.domain.repository.NetworkUsersRepository
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.domain.utils.Response
import sdumchykov.androidApp.domain.utils.Status
import java.io.IOException
import javax.inject.Inject

private const val PASSWORD = "PASSWORD"

@HiltViewModel
class CredentialsViewModel @Inject constructor(
    private val sharedPreferencesStorage: Storage,
    private val serverRepository: NetworkUsersRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _status = MutableLiveData<Response<Status>>()
    val status: LiveData<Response<Status>> = _status

    fun authorizeUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoadingStatus()
            val response = try {
                serverRepository.authorizeUser(AuthorizeModel(email, password))
            } catch (e: IOException) {
                setErrorStatus(R.string.messageIOException)
                return@launch
            } catch (e: HttpException) {
                setErrorStatus(R.string.messageHTTPException)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                val db = Room.databaseBuilder(
                    context, AppDatabase::class.java, "database-name"
                ).build()
                val userDao = db.userDao()
                if (userDao.getUser() != null) userDao.delete(userDao.getUser())

                val user = response.body()?.data?.user
                userDao.insert(
                    sdumchykov.androidApp.domain.local.User(
                        address = user?.address,
                        birthday = user?.birthday,
                        career = user?.career,
                        email = user?.email,
                        facebook = user?.facebook,
                        id = user?.id ?: 0,
                        image = user?.image,
                        instagram = user?.instagram,
                        linkedin = user?.linkedin,
                        name = user?.name,
                        phone = user?.phone,
                        twitter = user?.twitter
                    )
                )

                sharedPreferencesStorage.save(
                    Constants.ACCESS_TOKEN, response.body()?.data?.accessToken ?: ""
                )
                setSuccessStatus(Status.SUCCESS)
            } else {
                setErrorStatus(R.string.messageUnexpectedState)
            }
        }
    }


    fun register(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoadingStatus()
            val response = try {
                serverRepository.registerUser(email, password)
            } catch (e: IOException) {
                setErrorStatus(R.string.messageIOException)
                return@launch
            } catch (e: HttpException) {
                setErrorStatus(R.string.messageHTTPException)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                val db = Room.databaseBuilder(
                    context, AppDatabase::class.java, "database-name"
                ).build()
                val userDao = db.userDao()
                userDao.delete(userDao.getUser())
                //TODO extract database to DI

                val user = response.body()?.data?.user

                userDao.insert(
                    sdumchykov.androidApp.domain.local.User(
                        address = user?.address,
                        birthday = user?.birthday,
                        career = user?.career,
                        email = user?.email,
                        facebook = user?.facebook,
                        id = user?.id ?: 0,
                        image = user?.image,
                        instagram = user?.instagram,
                        linkedin = user?.linkedin,
                        name = user?.name,
                        phone = user?.phone,
                        twitter = user?.twitter
                    )
                )

                sharedPreferencesStorage.save(
                    Constants.ACCESS_TOKEN, response.body()?.data?.accessToken ?: ""
                )
                setSuccessStatus(Status.SUCCESS)
            } else {
                setErrorStatus(R.string.messageUnexpectedState)
            }
        }
    }

    private fun setSuccessStatus(status: Status) {
        _status.postValue(Response.success(status))
    }

    private fun setLoadingStatus() {
        _status.postValue(Response.loading(null))
    }

    private fun setErrorStatus(messageResourceId: Int) {
        _status.postValue(Response.error(messageResourceId, null))
    }

    fun savePassword(password: String) = sharedPreferencesStorage.save(PASSWORD, password)


    fun getPassword(): String = sharedPreferencesStorage.getString(PASSWORD) ?: ""

}
