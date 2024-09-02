package com.dumchykov.socialnetworkdemo.domain.webapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailPassword(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)