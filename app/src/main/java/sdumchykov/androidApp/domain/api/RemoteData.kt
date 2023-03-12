package sdumchykov.androidApp.domain.api

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
import javax.inject.Inject

class RemoteData @Inject constructor(private val serverApi: ServerApi) {

    suspend fun registerUser(
        email: String,
        password: String
    ): Response<ServerResponse<RegisterData>> =
        serverApi.registerUser(email, password)

    suspend fun editUser(
        userId: Int,
        token: String,
        body: EditProfileUser
    ): Response<ServerResponse<User>> =
        serverApi.editUser(userId, token, body)

    suspend fun authorizeUser(body: AuthorizeModel): Response<ServerResponse<AuthorizationData>> =
        serverApi.authorizeUser(body)

    suspend fun addContact(
        userId: Int,
        token: String,
        contactId: ContactIdModel
    ): Response<ServerResponse<Data>> =
        serverApi.addContact(userId, token, contactId)

    suspend fun getAccountUsers(userId: Int, token: String): Response<ServerResponse<Contacts>> =
        serverApi.getAccountUsers(userId, token)

    suspend fun deleteContact(
        userId: Int,
        contactId: Int,
        token: String
    ): Response<ServerResponse<Contacts>> =
        serverApi.deleteContact(userId, contactId, token)

    suspend fun getUsers(token: String): Response<ServerResponse<Data>> =
        serverApi.getUsers(token)
}