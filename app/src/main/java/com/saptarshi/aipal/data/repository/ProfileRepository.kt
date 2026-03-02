package com.saptarshi.aipal.data.repository

import com.saptarshi.aipal.data.local.db.dao.UserProfileDao
import com.saptarshi.aipal.data.local.db.entity.UserProfileEntity
import com.saptarshi.aipal.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {

    suspend fun getProfile(): User? {
        val entity = userProfileDao.getUserProfile()
        return entity?.let { User(it.id, it.email, it.name ?: "") }
    }

    suspend fun updatePhoto(photoPath: String) {
        val existing = userProfileDao.getUserProfile()
        if (existing != null) {
            userProfileDao.insertUserProfile(existing.copy(photoPath = photoPath))
        }
    }
}