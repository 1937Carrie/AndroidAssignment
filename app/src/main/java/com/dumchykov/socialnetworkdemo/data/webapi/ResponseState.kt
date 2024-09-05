package com.dumchykov.socialnetworkdemo.data.webapi

sealed interface ResponseState {
    data object Initial : ResponseState
    data object Loading : ResponseState
    data class Success<T>(val data: T) : ResponseState
    data class HttpCode(val code: Int, val message: String) : ResponseState
    data class Error(val errorMessage: String?) : ResponseState
}