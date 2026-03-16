package com.saptarshi.aipal.ui.chat

import androidx.lifecycle.ViewModel
import com.saptarshi.aipal.data.repository.ConversationRepository
import com.saptarshi.aipal.domain.model.Conversation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            conversationRepository.refreshConversations()
        }
    }

    val recentChats: StateFlow<List<Conversation>> =
        conversationRepository.getCachedConversation()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}
