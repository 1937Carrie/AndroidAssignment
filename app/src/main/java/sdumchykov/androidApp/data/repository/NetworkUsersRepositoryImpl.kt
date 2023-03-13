package sdumchykov.androidApp.data.repository

import retrofit2.Response
import sdumchykov.androidApp.domain.api.RemoteData
import sdumchykov.androidApp.domain.model.Data
import sdumchykov.androidApp.domain.model.ServerResponse
import sdumchykov.androidApp.domain.model.authorizeUser.AuthorizationData
import sdumchykov.androidApp.domain.model.contacts.Contacts
import sdumchykov.androidApp.domain.model.register.RegisterData
import sdumchykov.androidApp.domain.model.requestModels.AuthorizeModel
import sdumchykov.androidApp.domain.model.requestModels.ContactIdModel
import sdumchykov.androidApp.domain.model.requestModels.EditProfileResponseData
import sdumchykov.androidApp.domain.model.requestModels.EditProfileUser
import sdumchykov.androidApp.domain.repository.NetworkUsersRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUsersRepositoryImpl @Inject constructor(
    private val remoteData: RemoteData
) : NetworkUsersRepository {

    override suspend fun registerUser(
        email: String,
        password: String
    ): Response<ServerResponse<RegisterData>> =
        remoteData.registerUser(email, password)


    override suspend fun editUser(
        userId: Int,
        token: String,
        body: EditProfileUser
    ): Response<ServerResponse<EditProfileResponseData>> =
        remoteData.editUser(userId, token, body)

    override suspend fun authorizeUser(body: AuthorizeModel): Response<ServerResponse<AuthorizationData>> =
        remoteData.authorizeUser(body)

    override suspend fun addContact(
        userId: Int,
        token: String,
        contactId: ContactIdModel
    ): Response<ServerResponse<Data>> =
        remoteData.addContact(userId, token, contactId)

    override suspend fun getAccountUsers(
        userId: Int,
        token: String
    ): Response<ServerResponse<Contacts>> =
        remoteData.getAccountUsers(userId, token)

    override suspend fun deleteContact(
        userId: Int,
        contactId: Int,
        token: String
    ): Response<ServerResponse<Contacts>> =
        remoteData.deleteContact(userId, contactId, token)

    override suspend fun getUsers(token: String): Response<ServerResponse<Data>> =
        remoteData.getUsers(token)

}