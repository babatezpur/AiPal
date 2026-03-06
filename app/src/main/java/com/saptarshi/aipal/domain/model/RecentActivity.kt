package com.saptarshi.aipal.domain.model

data class RecentActivity(
    val id: Long,
    val topic: String,
    val category: String,
    val timestamp: Long
)