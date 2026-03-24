package com.saptarshi.aipal.data.repository

import com.saptarshi.aipal.data.local.db.dao.UserProfileDao
import com.saptarshi.aipal.data.local.db.entity.UserProfileEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {

    suspend fun getProfile(): UserProfileEntity? {
        return userProfileDao.getUserProfile()
    }

    suspend fun updatePhoto(photoPath: String) {
        val existing = userProfileDao.getUserProfile()
        if (existing != null) {
            userProfileDao.insertUserProfile(existing.copy(photoPath = photoPath))
        }
    }

    suspend fun updateName(name: String) {
        val existing = userProfileDao.getUserProfile()
        if (existing != null) {
            userProfileDao.insertUserProfile(existing.copy(name = name))
        }
    }

    suspend fun clearProfile() {
        userProfileDao.deleteUserProfile()
    }

}