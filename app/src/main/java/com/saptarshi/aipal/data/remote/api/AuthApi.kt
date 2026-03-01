package com.saptarshi.aipal.data.remote.api

import com.saptarshi.aipal.data.remote.dto.AuthResponse
import com.saptarshi.aipal.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: RegisterRequest
    ): AuthResponse


}