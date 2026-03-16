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
class ChatViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository,
) : ViewModel() {

    // Current conversation ID — null means new chat mode
    private var _conversationId: Int? = null

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

    /** Mode 2: Existing chat — load messages from backend */
    fun loadMessages(conversationId: Int) {
        _conversationId = conversationId
        viewModelScope.launch {
            _loadState.value = Resource.Loading
            when (val result = conversationRepository.getConversationMessages(conversationId)) {
                is Resource.Success -> {
                    _messages.value = result.data ?: emptyList()
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

    /** Mode 1: New chat — empty screen, ready for first message */
    fun initNewChat() {
        _conversationId = null
        _messages.value = emptyList()
        _messagesRemaining.value = 5
        _loadState.value = Resource.Success(Unit)
    }

    fun sendMessage(content: String) {
        if (content.isBlank() || _messagesRemaining.value <= 0) return

        viewModelScope.launch {
            // Add user message optimistically
            val userMsg = Message(0, _conversationId ?: 0, "user", content)
            _messages.value += userMsg
            _sendState.value = Resource.Loading

            if (_conversationId == null) {
                // New chat — POST /conversation/start
                when (val result = conversationRepository.startConversation(content)) {
                    is Resource.Success -> {
                        val data = result.data!!
                        _conversationId = data.conversationId
                        _messagesRemaining.value = data.messagesRemaining
                        val aiMsg = Message(0, data.conversationId, "assistant", data.reply)
                        _messages.value += aiMsg
                        _sendState.value = Resource.Success(Unit)
                    }
                    is Resource.Error -> {
                        _messages.value = _messages.value.dropLast(1)
                        _sendState.value = Resource.Error(result.message ?: "Failed to start conversation")
                    }
                    else -> {}
                }
            } else {
                // Existing chat — POST /conversation/message
                when (val result = conversationRepository.sendMessage(_conversationId!!, content)) {
                    is Resource.Success -> {
                        val aiMsg = result.data!!
                        _messages.value += aiMsg
                        _messagesRemaining.value -= 1
                        _sendState.value = Resource.Success(Unit)
                    }
                    is Resource.Error -> {
                        _messages.value = _messages.value.dropLast(1)
                        _sendState.value = Resource.Error(result.message ?: "Failed to send message")
                    }
                    else -> {}
                }
            }
        }
    }
}
