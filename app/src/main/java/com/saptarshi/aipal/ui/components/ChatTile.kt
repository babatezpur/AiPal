package com.saptarshi.aipal.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.MarkUnreadChatAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saptarshi.aipal.domain.model.Conversation
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatTile(
    chat: Conversation,
    onClick : () -> Unit = {}
) {


    // Date formatting
    // Formatter
    val formatter = DateTimeFormatter.ofPattern("dd MMMM")

    // Converting timestamp to LocalDateTime
    val localDateTimeSystemDefault: String? = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(chat.createdAt),
        ZoneId.systemDefault()
    ).format(formatter)


    val dateToday = LocalDateTime.now().format(formatter)

    val timestampReadable = if (localDateTimeSystemDefault == dateToday) "Today" else localDateTimeSystemDefault


    val icon = if (chat.messageCount < 5 ) Icons.Filled.MarkUnreadChatAlt else Icons.Filled.Chat

    Card(
        onClick = {
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )

    ) {
        Row(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end=30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ){
            Icon(
                modifier = Modifier
                    .size(28.dp),
                imageVector = icon,
                contentDescription = "Favorite Icon",
                tint = MaterialTheme.colorScheme.primary // Customize the icon color
            )


            // Show timestamp in human readable form
            Text(
                text = timestampReadable.toString() ,
                modifier = Modifier
                    .padding(top=10.dp, start=20.dp),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Restrict the topic to one line and then add a ... if it's too long
        Text(
            text = chat.title,
            modifier = Modifier
                .padding(top=5.dp, start=20.dp, end = 20.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
        
        Text(
            text = "Messages left: ${5 - chat.messageCount}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp, end = 30.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ChatPreview() {
    val chat = Conversation(
        id = 1,
        title = "This is a title which is very",
        createdAt = System.currentTimeMillis(),
        messageCount = 3
    )
    ChatTile(chat, {})

}