package com.dumchykov.socialnetworkdemo.data.webapi

import com.dumchykov.socialnetworkdemo.domain.webapi.ContactApiService
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import com.dumchykov.socialnetworkdemo.domain.webapi.models.Contact
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ContactId
import com.dumchykov.socialnetworkdemo.domain.webapi.models.EmailPassword

class ContactRepositoryImpl(
    private val contactApiService: ContactApiService,
) : ContactRepository {
    override suspend fun addContact(
        bearerToken: String,
        userId: Int,
        contactId: ContactId,
    ): ResponseState {
        try {
            val addContactResponse = contactApiService.addContact(bearerToken, userId, contactId)
            return if (addContactResponse.code == 200) {
                ResponseState.Success(addContactResponse.data)
            } else {
                ResponseState.HttpCode(addContactResponse.code, addContactResponse.message)
            }
        } catch (e: Throwable) {
            return ResponseState.Error(e.message)
        }
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

    override suspend fun deleteContact(
        userId: Int,
        contactId: Int,
        bearerToken: String,
    ): ResponseState {
        try {
            val deleteContactResponse =
                contactApiService.deleteContact(userId, contactId, bearerToken)
            return if (deleteContactResponse.code == 200) {
                ResponseState.Success(deleteContactResponse.data)
            } else {
                ResponseState.HttpCode(deleteContactResponse.code, deleteContactResponse.message)
            }
        } catch (e: Throwable) {
            return ResponseState.Error(e.message)
        }
    }

    override suspend fun editUser(userId: Int, bearerToken: String, user: Contact): ResponseState {
        try {
            val editUserResponse = contactApiService.editUser(userId, bearerToken, user)
            return if (editUserResponse.code == 200) {
                ResponseState.Success(editUserResponse.data)
            } else {
                ResponseState.HttpCode(editUserResponse.code, editUserResponse.message)
            }
        } catch (e: Throwable) {
            return ResponseState.Error(e.message)
        }
    }

    override suspend fun getUserById(userId: Int, bearerToken: String): ResponseState {
        try {
            val getUserResponse = contactApiService.getUser(userId, bearerToken)
            return if (getUserResponse.code == 200) {
                ResponseState.Success(getUserResponse.data)
            } else {
                ResponseState.HttpCode(getUserResponse.code, getUserResponse.message)
            }
        } catch (e: Throwable) {
            return ResponseState.Error(e.message)
        }
    }

    override suspend fun getUserContacts(userId: Int, bearerToken: String): ResponseState {
        try {
            val getUserContactsResponse = contactApiService.getUserContacts(userId, bearerToken)
            return if (getUserContactsResponse.code == 200) {
                ResponseState.Success(getUserContactsResponse.data)
            } else {
                ResponseState.HttpCode(
                    getUserContactsResponse.code,
                    getUserContactsResponse.message
                )
            }
        } catch (e: Throwable) {
            return ResponseState.Error(e.message)
        }
    }

    override suspend fun getUsers(bearerToken: String): ResponseState {
        try {
            val getUserResponse = contactApiService.getUsers(bearerToken)
            return if (getUserResponse.code == 200) {
                ResponseState.Success(getUserResponse.data)
            } else {
                ResponseState.HttpCode(getUserResponse.code, getUserResponse.message)
            }
        } catch (e: Throwable) {
            return ResponseState.Error(e.message)
        }
    }

    override suspend fun refreshToken(refreshToken: String): ResponseState {
        try {
            val refreshTokenResponse = contactApiService.refreshToken(refreshToken)
            return if (refreshTokenResponse.code == 200) {
                ResponseState.Success(refreshTokenResponse.data)
            } else {
                ResponseState.HttpCode(
                    refreshTokenResponse.code,
                    refreshTokenResponse.message
                )
            }
        } catch (e: Throwable) {
            return ResponseState.Error(e.message)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
    ): ResponseState {
        try {
            val registerResponse = contactApiService.register(email, password)
            return if (registerResponse.code == 200) {
                ResponseState.Success(registerResponse.data)
            } else {
                ResponseState.HttpCode(registerResponse.code, registerResponse.message)
            }
        } catch (e: Throwable) {
            return ResponseState.Error(e.message)
        }
    }
}