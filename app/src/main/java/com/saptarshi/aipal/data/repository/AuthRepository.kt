package com.saptarshi.aipal.data.repository

import com.saptarshi.aipal.data.local.datastore.TokenManager
import com.saptarshi.aipal.data.local.db.dao.UserProfileDao
import com.saptarshi.aipal.data.local.db.entity.UserProfileEntity
import com.saptarshi.aipal.data.remote.api.AuthApi
import com.saptarshi.aipal.data.remote.dto.LoginRequest
import com.saptarshi.aipal.data.remote.dto.RegisterRequest
import com.saptarshi.aipal.domain.model.User
import com.saptarshi.aipal.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
    private val userProfileDao: UserProfileDao,
) {
    suspend fun register(username: String, email: String, password: String) : Resource<User> {
        return try {
            val response = authApi.register(RegisterRequest(username, email, password))

            if (response.isSuccessful) {
                // update token manager
                tokenManager.saveToken(response.body()!!.token)
                // update profiledao

                userProfileDao.insertUserProfile(
                    UserProfileEntity(email=email, name=username, photoPath = null)
                )
                val body = response.body()!!
                Resource.Success<User>(User(body.user.id, body.user.email, body.user.username))
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun login(email: String, password: String): Resource<User> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()!!
                tokenManager.saveToken(body.token)
                userProfileDao.insertUserProfile(
                    UserProfileEntity(email = body.user.email, name = body.user.username, photoPath = null)
                )
                Resource.Success(User(body.user.id, body.user.email, body.user.username))
            } else {
                Resource.Error("Invalid email or password")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun logout() {
        tokenManager.clearToken()
        userProfileDao.deleteUserProfile()
    }

    fun isLoggedIn() = tokenManager.tokenFlow
}