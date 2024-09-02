package com.dumchykov.socialnetworkdemo.data.webapi

import com.dumchykov.socialnetworkdemo.domain.webapi.ContactApiService
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import com.dumchykov.socialnetworkdemo.domain.webapi.models.Contact
import com.dumchykov.socialnetworkdemo.domain.webapi.models.EmailPassword

class ContactRepositoryImpl(
    private val contactApiService: ContactApiService,
) : ContactRepository {
    override suspend fun addContact(contactId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun authorize(email: String, password: String): ResponseState {
        try {
            val authorizeResponse = contactApiService.authorize(EmailPassword(email, password))
            return if (authorizeResponse.code == 200) {
                ResponseState.Success(authorizeResponse.data)
            } else {
                ResponseState.HttpCode(authorizeResponse.code, authorizeResponse.message)
            }
        } catch (e: Throwable) {
            return ResponseState.Error(e.message)
        }
    }

    override suspend fun deleteContact(contactId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun editUser(user: Contact) {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUser(): Contact {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(userId: Int): Contact {
        TODO("Not yet implemented")
    }

    override suspend fun getUserContacts(): List<Contact> {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers() {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken() {
        TODO("Not yet implemented")
    }

    override suspend fun register(email: String, password: String, saveCredentials: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserContacts() {
        TODO("Not yet implemented")
    }
}