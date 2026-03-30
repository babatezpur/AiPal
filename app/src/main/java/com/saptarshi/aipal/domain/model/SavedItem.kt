package com.saptarshi.aipal.domain.model

data class SavedItem(
    val id: Int,
    val category: FeatureCategory,
    val content: String,
    val author: String?,
    val topic: String,
    val savedAt: Long
)