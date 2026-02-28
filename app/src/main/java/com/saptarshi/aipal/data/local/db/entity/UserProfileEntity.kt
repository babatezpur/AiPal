package com.saptarshi.aipal.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,         // Always 1 since this is a single row table
    val email: String,
    val name: String?,
    @ColumnInfo(name = "photo_path")
    val photoPath: String?
)