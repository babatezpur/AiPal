package com.saptarshi.aipal.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recent_activities")
data class RecentActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val topic: String,
    val category: String,       // this can be 'fact' or 'quote'
    val timestamp: Long
)