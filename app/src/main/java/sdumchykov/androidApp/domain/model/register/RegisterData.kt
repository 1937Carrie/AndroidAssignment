package sdumchykov.androidApp.domain.model.register

import sdumchykov.androidApp.domain.model.User

data class RegisterData(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)