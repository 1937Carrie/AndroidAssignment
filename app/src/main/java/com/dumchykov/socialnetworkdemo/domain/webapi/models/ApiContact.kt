package com.dumchykov.socialnetworkdemo.domain.webapi.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ApiContact(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String?,
    @SerialName("email") val email: String?,
    @SerialName("phone") val phone: String?,
    @SerialName("career") val career: String?,
    @SerialName("address") val address: String?,
    @SerialName("birthday") val birthday: String?,
    @SerialName("facebook") val facebook: String?,
    @SerialName("instagram") val instagram: String?,
    @SerialName("twitter") val twitter: String?,
    @SerialName("linkedin") val linkedin: String?,
    @SerialName("image") val image: String?,
    @SerialName("created_at") val created_at: String,
    @SerialName("updated_at") val updated_at: String,
)