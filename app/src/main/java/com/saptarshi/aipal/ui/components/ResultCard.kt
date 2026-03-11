package com.saptarshi.aipal.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saptarshi.aipal.domain.model.FeatureCategory
import com.saptarshi.aipal.domain.model.SearchResult

@Composable
fun ResultCard(
    result: SearchResult,
    isSaved: Boolean = false,
    onSaveClick: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Main content text
            Text(
                text = result.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Author line — only visible for quotes
            if (result.author != null) {
                Text(
                    text = "— ${result.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Save button row — aligned to the end
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category icon
                Icon(
                    imageVector = if (result.category == FeatureCategory.FACT)
                        Icons.Filled.Lightbulb else Icons.Filled.FormatQuote,
                    contentDescription = if (result.category == FeatureCategory.FACT) "Fact" else "Quote",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Save/favourite button
                IconButton(onClick = onSaveClick) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isSaved) "Saved" else "Save",
                        tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultCardFactPreview() {
    ResultCard(
        result = SearchResult(
            content = "A black hole forms when a massive star collapses under its own gravity at the end of its life cycle.",
            author = null,
            topic = "black holes",
            category = FeatureCategory.FACT
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ResultCardQuotePreview() {
    ResultCard(
        result = SearchResult(
            content = "I've missed more than 9000 shots in my career. I've lost almost 300 games.",
            author = "Michael Jordan",
            topic = "perseverance",
            category = FeatureCategory.QUOTE
        ),
        isSaved = true
    )
}