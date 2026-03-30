package com.saptarshi.aipal.ui.components

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.saptarshi.aipal.domain.model.FeatureCategory
import com.saptarshi.aipal.domain.model.SavedItem


@Composable
fun FavouritesTile(
    item: SavedItem,
    scale: Float,
    pageOffset: Float,
    onUnsaveClick: () -> Unit = {}
) {

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var showFullContent by remember { mutableStateOf(false) }

    val icon = if (item.category == FeatureCategory.FACT)
        Icons.Filled.Lightbulb
    else
        Icons.Filled.FormatQuote

    val contentToCopy = if (item.category == FeatureCategory.FACT) {
        item.content
    } else {
        "${item.content} — ${item.author ?: "Unknown"}"
    }

    Column(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = lerp(0.5f, 1f, 1f - pageOffset)
            }
            .aspectRatio(0.75f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Blue)
            .padding(horizontal = 15.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                modifier = Modifier
                    .size(28.dp),
                imageVector = icon,
                contentDescription = "Favorite Icon",
                tint = Color.White
            )

            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .clickable(true) {
                        clipboardManager.setText(AnnotatedString(contentToCopy))
                        val label = if (item.category == FeatureCategory.FACT) "Fact" else "Quote"
                        Toast.makeText(context, "$label copied to Clipboard", Toast.LENGTH_SHORT).show()
                    },
                imageVector = Icons.Filled.ContentCopy,
                contentDescription = "Copy Icon",
                tint = Color.White
            )
        }

        Text(
            modifier = Modifier
                .weight(1f)
                .clickable { showFullContent = true },
            text = item.content,
            color = Color.White,
            fontSize = 24.sp,
            lineHeight = 28.sp,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )

        if (showFullContent) {
            AlertDialog(
                onDismissRequest = { showFullContent = false },
                title = {
                    Text(
                        item.topic.capitalize()
                    )
                },
                text = { Text(item.content) },
                confirmButton = {
                    TextButton(onClick = { showFullContent = false }) {
                        Text("Close")
                    }
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            // Save/favourite button
            IconButton(onClick = onUnsaveClick) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Filled.Favorite,
                    contentDescription =  "Saved",
                    tint =  MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
