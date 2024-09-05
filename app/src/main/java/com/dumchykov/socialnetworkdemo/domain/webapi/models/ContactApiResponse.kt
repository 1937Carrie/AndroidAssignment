package com.dumchykov.socialnetworkdemo.domain.webapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactApiResponse<T>(
    @SerialName("status") val status: String,
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
    @SerialName("data") val data: T,
)