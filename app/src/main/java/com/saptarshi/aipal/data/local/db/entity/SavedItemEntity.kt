package com.saptarshi.aipal.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_items")
data class SavedItemEntity(
    @PrimaryKey
    val id: Int,             // Backend ID, not auto-generated
    val category: String,    // "fact" or "quote"
    val content: String,
    val author: String?,
    val topic: String,
    @ColumnInfo(name = "saved_at")
    val savedAt: Long
)