package com.saptarshi.aipal.domain.model

data class SearchResult(
    val content: String,
    val author: String?,
    val topic: String,
    val category: FeatureCategory
)