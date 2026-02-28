package com.saptarshi.aipal.domain.model

data class Message(
    val id: Int,
    val conversationId: Int,
    val role: String,       // "user" or "assistant"
    val content: String,
    val createdAt: Long
)