package com.dumchykov.socialnetworkdemo.domain.webapi

import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.webapi.models.Contact

interface ContactRepository {
    suspend fun addContact(contactId: Int): Boolean
    suspend fun authorize(email: String, password: String): ResponseState
    suspend fun deleteContact(contactId: Int): Boolean
    suspend fun editUser(user: Contact)
    suspend fun getCurrentUser(): Contact
    suspend fun getUserById(userId: Int): Contact
    suspend fun getUserContacts(): List<Contact>
    suspend fun getUsers()
    suspend fun refreshToken()
    suspend fun register(email: String, password: String, saveCredentials: Boolean = false)
    suspend fun updateUserContacts()
}