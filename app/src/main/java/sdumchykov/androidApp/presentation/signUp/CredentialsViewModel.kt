package sdumchykov.androidApp.presentation.signUp

import android.content.Context
import android.content.res.Resources
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sdumchykov.androidApp.R
import sdumchykov.androidApp.data.repository.ServerApiRepositoryImpl
import sdumchykov.androidApp.databinding.FragmentLogInBinding
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.domain.model.requestModels.AuthorizeModel
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CredentialsViewModel @Inject constructor(
    private val sharedPreferencesStorage: Storage,
    private val serverApi: ServerApiRepositoryImpl,
    @ApplicationContext private val context: Context
) : ViewModel() {

    fun authorizeUser(binding: FragmentLogInBinding, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            with(binding) {
                textViewLogInAuthorizeResponseText.isVisible = false

                val response = try {
                    serverApi.authorizeUser(AuthorizeModel(email, password))
                } catch (e: IOException) {
//                    progressBarLogIn.post { progressBarLogIn.isVisible = false }
                    return@launch
                } catch (e: HttpException) {
//                    progressBarLogIn.post { progressBarLogIn.isVisible = false }
                    return@launch
                }

                if (response.isSuccessful && response.body() != null) {
                    val db = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java, "database-name"
                    ).build()
                    val userDao = db.userDao()
                    if (userDao.getUser() != null) userDao.delete(userDao.getUser())

                    userDao.insert(
                        sdumchykov.androidApp.domain.local.User(
                            address = response.body()?.data?.user?.address,
                            birthday = response.body()?.data?.user?.birthday,
                            career = response.body()?.data?.user?.career,
                            email = response.body()?.data?.user?.email,
                            facebook = response.body()?.data?.user?.facebook,
                            id = response.body()?.data?.user?.id ?: 0,
                            image = response.body()?.data?.user?.image,
                            instagram = response.body()?.data?.user?.instagram,
                            linkedin = response.body()?.data?.user?.linkedin,
                            name = response.body()?.data?.user?.name,
                            phone = response.body()?.data?.user?.phone,
                            twitter = response.body()?.data?.user?.twitter
                        )
                    )

                    sharedPreferencesStorage.save(
                        Constants.ACCESS_TOKEN,
                        response.body()?.data?.accessToken ?: ""
                    )
                } else {
                    textViewLogInAuthorizeResponseText.text =
                        Resources.getSystem().getString(R.string.emailAuthErrorMessage)
                    textViewLogInAuthorizeResponseText.isVisible = true
                }
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                serverApi.registerUser(email, password)
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                val db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "database-name"
                ).build()
                val userDao = db.userDao()
                userDao.delete(userDao.getUser())


                userDao.insert(
                    sdumchykov.androidApp.domain.local.User(
                        address = response.body()?.data?.user?.address,
                        birthday = response.body()?.data?.user?.birthday,
                        career = response.body()?.data?.user?.career,
                        email = response.body()?.data?.user?.email,
                        facebook = response.body()?.data?.user?.facebook,
                        id = response.body()?.data?.user?.id ?: 0,
                        image = response.body()?.data?.user?.image,
                        instagram = response.body()?.data?.user?.instagram,
                        linkedin = response.body()?.data?.user?.linkedin,
                        name = response.body()?.data?.user?.name,
                        phone = response.body()?.data?.user?.phone,
                        twitter = response.body()?.data?.user?.twitter
                    )
                )

                sharedPreferencesStorage.save(
                    Constants.ACCESS_TOKEN,
                    response.body()?.data?.accessToken ?: ""
                )
            }
        }
    }
}
