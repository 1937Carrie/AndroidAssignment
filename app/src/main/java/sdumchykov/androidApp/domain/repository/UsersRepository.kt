package sdumchykov.androidApp.domain.repository

import sdumchykov.androidApp.domain.model.UserModel

interface UsersRepository {
    suspend fun getHardcodedUsers(): List<UserModel>
    suspend fun getRealUsers(): List<UserModel>
}
