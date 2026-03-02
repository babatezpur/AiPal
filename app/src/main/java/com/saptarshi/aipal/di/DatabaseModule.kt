package com.saptarshi.aipal.di

import android.content.Context
import androidx.room.Room
import com.saptarshi.aipal.data.local.db.AppDatabase
import com.saptarshi.aipal.data.local.db.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideConversationDao(db: AppDatabase): ConversationDao {
        return db.conversationDao()
    }

    @Provides
    fun provideSavedItemDao(db: AppDatabase): SavedItemDao {
        return db.savedItemDao()
    }

    @Provides
    fun provideRecentActivityDao(db: AppDatabase): RecentActivityDao {
        return db.recentActivityDao()
    }

    @Provides
    fun provideUserProfileDao(db: AppDatabase): UserProfileDao {
        return db.userProfileDao()
    }
}