package sdumchykov.androidApp.domain.model.authorizeUser

import sdumchykov.androidApp.domain.model.User

data class AuthorizationData(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)