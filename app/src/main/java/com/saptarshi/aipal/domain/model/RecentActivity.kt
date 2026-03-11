package com.saptarshi.aipal.domain.model

data class RecentActivity(
    val id: Long,
    val topic: String,
    val category: FeatureCategory,
    val timestamp: Long
)

enum class FeatureCategory(val value: String) {
    FACT("fact"),
    QUOTE("quote");

    companion object {
        fun fromString(value: String): FeatureCategory {
            return when (value) {
                "fact" -> FACT
                "quote" -> QUOTE
                else -> throw IllegalArgumentException("Invalid FeatureCategory value: $value")
            }
        }
    }
}