package com.saptarshi.aipal.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.saptarshi.aipal.data.local.db.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    suspend fun getConversationById(conversationId: Int): ConversationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Query("SELECT * FROM conversations ORDER BY created_at DESC LIMIT 10")
    fun getRecentConversations(): Flow<List<ConversationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)

    @Query("DELETE FROM conversations WHERE id not in (SELECT id FROM conversations ORDER BY created_at DESC LIMIT 30)")
    suspend fun deleteOldConversations()

    @Transaction
    suspend fun insertAndCleanup(conversation: ConversationEntity) {
        insertConversation(conversation)
        deleteOldConversations()
    }

}