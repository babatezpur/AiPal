package com.saptarshi.aipal.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val message: String,
    val user: UserDto,
    val token: String
)

data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("created_at")
    val createdAt: String
)
