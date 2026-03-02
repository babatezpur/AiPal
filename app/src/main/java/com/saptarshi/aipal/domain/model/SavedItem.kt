package com.saptarshi.aipal.domain.model

data class SavedItem(
    val id: Int,
    val category: String,
    val content: String,
    val author: String?,
    val topic: String,
    val savedAt: Long
)