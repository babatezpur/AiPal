package com.saptarshi.aipal.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saptarshi.aipal.data.local.db.entity.SavedItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDao {

    @Query("SELECT * FROM saved_items ORDER BY saved_at DESC LIMIT 10")
    fun getRecentSavedItems(): Flow<List<SavedItemEntity>>

    @Query("DELETE FROM saved_items WHERE id = :itemId")
    suspend fun deleteSavedItem(itemId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedItem(item: SavedItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedItems(items: List<SavedItemEntity>)

    @Query("DELETE FROM saved_items")
    suspend fun deleteAllSavedItems()
}