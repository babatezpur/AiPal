package com.saptarshi.aipal.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.saptarshi.aipal.data.local.db.entity.RecentActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentActivityDao {
    @Query("SELECT * FROM recent_activities ORDER BY timestamp DESC LIMIT 10")
    fun getRecentActivities(): Flow<List<RecentActivityEntity>>

    @Insert
    suspend fun insertRecentActivity(activity: RecentActivityEntity)

    @Query("DELETE FROM recent_activities WHERE id not in (SELECT id FROM recent_activities ORDER BY timestamp DESC LIMIT 20)")
    suspend fun deleteOldRecentActivities()


    // Insert and delete - do them together
    @Transaction
    suspend fun insertAndCleanup(activity : RecentActivityEntity) {
        insertRecentActivity(activity)
        deleteOldRecentActivities()
    }
}