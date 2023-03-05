package sdumchykov.androidApp.domain.repository

import sdumchykov.androidApp.domain.model.User

interface UsersRepository {
    suspend fun getHardcodedUsers(): List<User>
    suspend fun getRealUsers(): List<User>
}
