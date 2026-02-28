package com.saptarshi.aipal.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saptarshi.aipal.data.local.db.dao.ConversationDao
import com.saptarshi.aipal.data.local.db.dao.RecentActivityDao
import com.saptarshi.aipal.data.local.db.dao.SavedItemDao
import com.saptarshi.aipal.data.local.db.dao.UserProfileDao
import com.saptarshi.aipal.data.local.db.entity.ConversationEntity
import com.saptarshi.aipal.data.local.db.entity.RecentActivityEntity
import com.saptarshi.aipal.data.local.db.entity.SavedItemEntity
import com.saptarshi.aipal.data.local.db.entity.UserProfileEntity

@Database(
    version = 1,
    entities = [
        ConversationEntity::class,
        SavedItemEntity::class,
        RecentActivityEntity::class,
        UserProfileEntity::class
    ],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun conversationDao(): ConversationDao
    abstract fun savedItemDao(): SavedItemDao
    abstract fun recentActivityDao(): RecentActivityDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        const val DATABASE_NAME = "aipal_db"
    }
}