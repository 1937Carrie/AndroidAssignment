package com.dumchykov.socialnetworkdemo.domain.webapi

import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ApiContact
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ContactId

interface ContactRepository {
    suspend fun addContact(bearerToken: String, userId: Int, contactId: ContactId): ResponseState
    suspend fun authorize(email: String, password: String): ResponseState
    suspend fun deleteContact(userId: Int, contactId: Int, bearerToken: String): ResponseState
    suspend fun editUser(userId: Int, bearerToken: String, user: ApiContact): ResponseState
    suspend fun getUserById(userId: Int, bearerToken: String): ResponseState
    suspend fun getUserContacts(userId: Int, bearerToken: String): ResponseState
    suspend fun getUsers(bearerToken: String): ResponseState
    suspend fun refreshToken(refreshToken: String): ResponseState
    suspend fun register(email: String, password: String): ResponseState
}