package com.dumchykov.socialnetworkdemo.data.webapi

import okhttp3.Interceptor
import okhttp3.Response

class BearerTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHeaders = originalRequest.headers

        // Check if the request contains an "Authorization" header
        if (originalHeaders["Authorization"] != null) {
            // Modify the Authorization header to add "Bearer " prefix
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer ${originalHeaders["Authorization"]}")
                .build()

            return chain.proceed(newRequest)
        }

        // Proceed with the original request if no "Authorization" header is present
        return chain.proceed(originalRequest)
    }
}