package com.saptarshi.aipal.domain.model

data class Conversation(
    val id: Int,
    val title: String,
    val createdAt: Long,
    val messageCount: Int,
)
