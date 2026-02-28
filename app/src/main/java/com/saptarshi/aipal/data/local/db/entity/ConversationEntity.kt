package com.saptarshi.aipal.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val id: Int,        // Bakcend ID, not auto-gen
    val title: String,
    @ColumnInfo(name = "message_count")
    val messageCount: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: String
)
