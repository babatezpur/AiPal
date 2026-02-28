package com.saptarshi.aipal.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ConversationStartResponse(
    @SerializedName("conversation_id")
    val conversationId: String,
    val message: String
)


data class SendMessageRequest(
    @SerializedName("conversation_id")
    val conversationId: Int,
    val message: String
)

data class SendMessageResponse(
    @SerializedName("conversation_id")
    val conversationId: Int,
    val reply: String,
    @SerializedName("messages_remaining")
    val messagesRemaining: Int
)

data class ConversationDto(
    val id: Int,
    val title: String,
    val messages: List<MessageDto>,
    @SerializedName("created_at")
    val createdAt: String
)

data class MessageDto(
    val id: Int,
    @SerializedName("conversation_id")
    val conversationId: Int,
    val role: String,
    val content: String,
    @SerializedName("created_at")
    val createdAt: String
)
