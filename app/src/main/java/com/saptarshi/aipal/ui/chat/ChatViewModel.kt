package com.saptarshi.aipal.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saptarshi.aipal.data.repository.ConversationRepository
import com.saptarshi.aipal.domain.model.Message
import com.saptarshi.aipal.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor (
    private val conversationRepository: ConversationRepository,
) : ViewModel(){
    // All messages in the conversation
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    // Loading state for initial fetch
    private val _loadState = MutableStateFlow<Resource<Unit>?>(null)
    val loadState: StateFlow<Resource<Unit>?> = _loadState.asStateFlow()

    // Sending state — shows typing indicator
    private val _sendState = MutableStateFlow<Resource<Unit>?>(null)
    val sendState: StateFlow<Resource<Unit>?> = _sendState.asStateFlow()

    // Messages remaining (out of 5)
    private val _messagesRemaining = MutableStateFlow(5)
    val messagesRemaining: StateFlow<Int> = _messagesRemaining.asStateFlow()


    fun loadMessages(conversationId: Int) {
        viewModelScope.launch {
            _loadState.value = Resource.Loading
            when (val result = conversationRepository.getConversationMessages(conversationId)) {
                is Resource.Success -> {
                    _messages.value = result.data ?: emptyList()
                    // Calculate remaining from how many user messages exist
                    val userMessages = _messages.value.count { it.role == "user" }
                    _messagesRemaining.value = 5 - userMessages
                    _loadState.value = Resource.Success(Unit)
                }
                is Resource.Error -> {
                    _loadState.value = Resource.Error(result.message ?: "Unknown error")
                }
                else -> {}

            }

        }
    }

    fun sendMessage(conversationId: Int, content: String) {

        if (content.isBlank() || _messagesRemaining.value <= 0) return

        viewModelScope.launch {

            val message = Message(
                id = 0,
                conversationId = conversationId,
                role = "user",
                content = content,
            )

            _messages.value += message

            _sendState.value = Resource.Loading

            when (val result = conversationRepository.sendMessage(conversationId, content)) {
                is Resource.Success -> {
                    // Append new messages to the list

                    val aiMessage = result.data!!
                    _messages.value += aiMessage
                    _messagesRemaining.value -= 1
                    _sendState.value = Resource.Success(Unit)
                }
                is Resource.Error -> {
                    _messages.value = _messages.value.dropLast(1)
                    _sendState.value = Resource.Error(result.message ?: "Unknown error")
                }
                else -> {}
            }
        }
    }

}