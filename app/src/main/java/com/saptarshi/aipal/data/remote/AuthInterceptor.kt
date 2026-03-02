package com.saptarshi.aipal.data.remote

import com.saptarshi.aipal.data.local.datastore.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject


class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
): Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val token = tokenManager.getTokenBlocking()

        val newRequest = if (token!= null ) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            } else request

        return chain.proceed(newRequest)

    }
}