package sdumchykov.androidApp.domain.repository

import retrofit2.Response
import sdumchykov.androidApp.domain.model.Data
import sdumchykov.androidApp.domain.model.ServerResponse
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.domain.model.authorizeUser.AuthorizationData
import sdumchykov.androidApp.domain.model.contacts.Contacts
import sdumchykov.androidApp.domain.model.register.RegisterData
import sdumchykov.androidApp.domain.model.requestModels.AuthorizeModel
import sdumchykov.androidApp.domain.model.requestModels.ContactIdModel
import sdumchykov.androidApp.domain.model.requestModels.EditProfileUser

interface ServerApiRepository {

    suspend fun registerUser(
        email: String,
        password: String
    ): Response<ServerResponse<RegisterData>>

    suspend fun editUser(
        userId: Int,
        token: String,
        body: EditProfileUser
    ): Response<ServerResponse<User>>

    suspend fun authorizeUser(body: AuthorizeModel): Response<ServerResponse<AuthorizationData>>

    suspend fun addContact(
        userId: Int,
        token: String,
        contactId: ContactIdModel
    ): Response<ServerResponse<Data>>

    suspend fun getAccountUsers(
        userId: Int,
        token: String
    ): Response<ServerResponse<Contacts>>

    suspend fun deleteContact(
        userId: Int,
        contactId: Int,
        token: String
    ): Response<ServerResponse<Contacts>>

    suspend fun getUsers(token: String): Response<ServerResponse<Data>>

}