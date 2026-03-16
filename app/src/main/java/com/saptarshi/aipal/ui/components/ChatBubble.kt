package com.saptarshi.aipal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saptarshi.aipal.domain.model.Message
import java.nio.file.WatchEvent

@Composable
fun ChatBubble (
    message: Message
) {

    val isUser = message.role == "user"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start  = if (isUser) 64.dp else 8.dp,
                end = if (isUser) 8.dp else 64.dp,
                top = 4.dp,
                bottom = 4.dp
            ),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isUser)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                // Sharp corner on the sender's side, rounded on the other
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isUser)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatBubbleUserPreview() {
    ChatBubble(
        message = Message(
            id = 1,
            conversationId = 1,
            role = "user",
            content = "What is the best way to learn guitar?",
            createdAt = System.currentTimeMillis()
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ChatBubbleAiPreview() {
    ChatBubble(
        message = Message(
            id = 2,
            conversationId = 1,
            role = "assistant",
            content = "Here are some effective approaches to learning guitar. Start with basic chords like G, C, D, and E minor. Practice for at least 15 minutes daily.",
            createdAt = System.currentTimeMillis()
        )
    )
}

