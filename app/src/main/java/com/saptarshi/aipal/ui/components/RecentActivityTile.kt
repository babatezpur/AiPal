package com.saptarshi.aipal.ui.components

import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toString
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saptarshi.aipal.domain.model.FeatureCategory
import com.saptarshi.aipal.domain.model.RecentActivity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.FormatQuote

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun RecentActivityTile(factOrQuote: RecentActivity = RecentActivity(
    12,
    "Topic is very long this time, cause we are tring to test out the ",
    FeatureCategory.FACT, 1772893614000
)) {


    // Date formatting
    // Formatter
    val formatter = DateTimeFormatter.ofPattern("d MMMM")

    // Converting timestamp to LocalDateTime
    val localDateTimeSystemDefault: String? = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(factOrQuote.timestamp),
        ZoneId.systemDefault()
    ).format(formatter)


    val dateToday = LocalDateTime.now().format(formatter)

    val timestampReadable = if (localDateTimeSystemDefault == dateToday) "Today" else localDateTimeSystemDefault


    val icon = if (factOrQuote.category == FeatureCategory.FACT)
        Icons.Filled.Lightbulb
    else
        Icons.Filled.FormatQuote




    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)

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
            text = factOrQuote.topic,
            modifier = Modifier
                .padding(top=5.dp, start=20.dp, end = 20.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}