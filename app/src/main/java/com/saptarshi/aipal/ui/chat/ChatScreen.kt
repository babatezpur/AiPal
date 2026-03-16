package com.saptarshi.aipal.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saptarshi.aipal.domain.model.Message
import com.saptarshi.aipal.ui.components.ChatBubble
import com.saptarshi.aipal.utils.Resource

@Composable
fun ChatScreen(
    conversationId: Int?,
    onBackClick: () -> Unit,
    viewmodel: ChatViewModel = hiltViewModel()
) {

    val messages by viewmodel.messages.collectAsState()
    val loadState by viewmodel.loadState.collectAsState()
    val sendState by viewmodel.sendState.collectAsState()
    val messagesRemaining by viewmodel.messagesRemaining.collectAsState()

    LaunchedEffect(conversationId) {
        if (conversationId != null) {
            viewmodel.loadMessages(conversationId)
        } else {
            viewmodel.initNewChat()
        }
    }

    ChatScreenContent(
        messages = messages,
        loadState = loadState,
        isSending = sendState is Resource.Loading,
        sendError = (sendState as? Resource.Error)?.message,
        onSendClick = { msg: String ->
            viewmodel.sendMessage(msg)
        },
        onBackClick = onBackClick,
        messagesRemaining = messagesRemaining,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenContent(
    messages: List<Message>,
    loadState: Resource<Unit>? = null,
    isSending: Boolean = false,
    sendError: String? = null,
    onSendClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    messagesRemaining: Int = 5
) {
    var inputText by rememberSaveable() { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size - 1)
    }

    Column {
        TopAppBar(
            title = {
                Column {
                    Text("Chat")
                    Text(
                        text = if (messagesRemaining > 0)
                            "$messagesRemaining messages remaining"
                        else
                            "Message limit reached",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (messagesRemaining > 0)
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        else
                            MaterialTheme.colorScheme.error
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {

            when (loadState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(40.dp)
                    )
                }
                is Resource.Error -> {
                    Text(
                        text = loadState.message ?: "Failed to load messages",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                is Resource.Success -> {
                    if (messages.isEmpty()) {
                        Text(
                            text = "Start the conversation!\nAsk anything.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    } else {

                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(messages) { message ->
                                ChatBubble(message = message)
                            }

                            // Typing indicator while waiting for AI response
                            if (isSending) {
                                item {
                                    TypingIndicator()
                                }
                            }
                        }

                    }
                }
                else -> {}

            }
        }

        // Error message for send failures
        if (sendError != null) {
            Text(
                text = sendError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text(
                        if (messagesRemaining > 0)
                            "Type a message..."
                        else
                            "Message limit reached"
                    )
                },
                enabled = messagesRemaining > 0 && !isSending,
                modifier = Modifier.weight(1f),
                singleLine = false,
                maxLines = 3
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        onSendClick(inputText)
                        inputText = ""
                    }
                },
                enabled = inputText.isNotBlank() && messagesRemaining > 0 && !isSending
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (inputText.isNotBlank() && messagesRemaining > 0 && !isSending)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

    }
}


// Simple typing indicator — three dots or a subtle animation
@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "AI is thinking...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ChatScreenEmptyPreview() {
    ChatScreenContent(
        messages = emptyList(),
        loadState = Resource.Success(Unit),
        messagesRemaining = 5
    )
}

@Preview(showBackground = true)
@Composable
fun ChatScreenWithMessagesPreview() {
    ChatScreenContent(
        messages = listOf(
            Message(1, 1, "user", "What is the best way to learn guitar?", System.currentTimeMillis()),
            Message(2, 1, "assistant", "Here are some effective approaches to learning guitar. Start with basic chords like G, C, D, and E minor. Practice for at least 15 minutes daily.", System.currentTimeMillis()),
            Message(3, 1, "user", "What about music theory?", System.currentTimeMillis()),
        ),
        loadState = Resource.Success(Unit),
        messagesRemaining = 3
    )
}
