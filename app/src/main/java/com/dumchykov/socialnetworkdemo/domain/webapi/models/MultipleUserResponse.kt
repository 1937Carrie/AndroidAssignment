package com.dumchykov.socialnetworkdemo.domain.webapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultipleUserResponse(
    @SerialName("users") val users: List<Contact>,
)