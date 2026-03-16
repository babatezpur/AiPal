package com.saptarshi.aipal.data.repository

import com.saptarshi.aipal.data.local.db.dao.ConversationDao
import com.saptarshi.aipal.data.local.db.entity.ConversationEntity
import com.saptarshi.aipal.data.remote.api.ConversationApi
import com.saptarshi.aipal.data.remote.dto.StartConversationRequest
import com.saptarshi.aipal.data.remote.dto.SendMessageRequest
import com.saptarshi.aipal.domain.model.Conversation
import com.saptarshi.aipal.domain.model.Message
import com.saptarshi.aipal.domain.model.StartResult
import com.saptarshi.aipal.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ConversationRepository @Inject constructor(
    private val conversationApi: ConversationApi,
    private val conversationDao: ConversationDao,
) {
    fun getCachedConversation() : Flow<List<Conversation>> {
        return conversationDao.getRecentConversations().map { entity ->
            entity.map { Conversation(it.id, it.title, it.createdAt, it.messageCount) }
        }
    }

    suspend fun refreshConversations(): Resource<List<Conversation>> {
        return try {
            val response = conversationApi.getConversations()
            if (response.isSuccessful) {
                val conversations = response.body()!!
                val entities = conversations.map {
                    ConversationEntity(
                        id = it.id,
                        title = it.title,
                        messageCount = it.messages.size,
                        createdAt = System.currentTimeMillis()
                    )
                }
                conversationDao.insertConversations(entities)
                conversationDao.deleteOldConversations()
                Resource.Success(conversations.map { Conversation(it.id, it.title, System.currentTimeMillis(), it.messages.size) })
            } else {
                Resource.Error("Failed to fetch conversations")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun startConversation(message: String): Resource<StartResult> {
        return try {
            val response = conversationApi.startConversation(StartConversationRequest(message))
            if (response.isSuccessful) {
                val body = response.body()!!
                Resource.Success(
                    StartResult(body.conversationId, body.reply, body.messagesRemaining)
                )
            } else {
                val errorMsg = if (response.code() == 429) "Daily limit reached" else "Failed to start conversation"
                Resource.Error(errorMsg)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getConversationMessages(conversationId: Int): Resource<List<Message>> {
        return try {
            val response = conversationApi.getConversation(conversationId)
            if (response.isSuccessful) {
                val convo = response.body()!!
                val messages = convo.messages.map {
                    Message(it.id, it.conversationId, it.role, it.content, System.currentTimeMillis())
                }
                Resource.Success(messages)
            } else {
                Resource.Error("Failed to fetch messages")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun sendMessage(conversationId: Int, message: String): Resource<Message> {
        return try {
            val response = conversationApi.sendMessage(SendMessageRequest(conversationId, message))
            if (response.isSuccessful) {
                val body = response.body()!!
                Resource.Success(
                    Message(0, conversationId, "assistant", body.reply, System.currentTimeMillis())
                )
            } else {
                val errorMsg = when (response.code()) {
                    429 -> "Daily limit reached"
                    400 -> "Message limit reached for this conversation"
                    else -> "Failed to send message"
                }
                Resource.Error(errorMsg)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

}