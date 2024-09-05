package com.dumchykov.socialnetworkdemo.domain.webapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleUserResponse(
    @SerialName("user") val user: ApiContact,
)