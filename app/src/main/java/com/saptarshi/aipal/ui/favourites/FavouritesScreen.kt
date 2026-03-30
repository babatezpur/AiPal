package com.saptarshi.aipal.ui.favourites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.painterResource
import com.saptarshi.aipal.R
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saptarshi.aipal.domain.model.SavedItem
import com.saptarshi.aipal.domain.model.FeatureCategory
import com.saptarshi.aipal.ui.components.FavouritesTile
import com.saptarshi.aipal.ui.theme.AiPalTheme
import kotlin.math.absoluteValue

@Composable
fun FavouritesScreen(
    viewModel : FavouritesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val favourites by viewModel.favourites.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    FavouriteScreenContent(
        favourites,
        { item ->
            viewModel.removeFromFavourites(item)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreenContent(
    favourites: List<SavedItem>,
    onUnsaveClick: (SavedItem) -> Unit = {}
) {

    var isCarouselView by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            title = { Text("Favourites") }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChip(
                selected = isCarouselView,
                onClick = { isCarouselView = true },
                label = { Text("Carousel") },
                leadingIcon = { Icon(painterResource(R.drawable.ic_carousel), contentDescription = null) }
            )
            FilterChip(
                selected = !isCarouselView,
                onClick = { isCarouselView = false },
                label = { Text("List") },
                leadingIcon = { Icon(Icons.Default.ViewList, contentDescription = null) }
            )
        }

        if (isCarouselView) {
            Carousel(
                favourites,
                onUnsaveClick
            )
        } else {
            DataListView(
                favourites
            )
        }
    }
}

@Composable
fun DataListView(
    data: List<SavedItem>,
    onUnsaveClick: (SavedItem) -> Unit = {}
) {


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        items(data.size) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Saved Item #${data[index].content}",
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(
    data: List<SavedItem>,
    onUnsaveClick: (SavedItem) -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { data.size })

    HorizontalPager(
        modifier = Modifier
            .fillMaxHeight(),
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 64.dp), // shows side items
        pageSpacing = 4.dp
    ) { page ->

        val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                ).absoluteValue

        val scale = lerp(
            start = 0.75f,
            stop = 1f,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        )

        FavouritesTile(
            item = data[page],
            scale = scale,
            pageOffset,
            onUnsaveClick = {
                onUnsaveClick(data[page])
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavouriteScreenPreview() {
    AiPalTheme {
        FavouriteScreenContent(
            getSavedItems()
        )
    }
}

fun getSavedItems(): List<SavedItem> {
    val items = mutableListOf<SavedItem>(
        SavedItem(
            id = 1,
            category = FeatureCategory.FACT,
            content = "The human brain uses about 20% of the body's total energy, despite making up only 2% of body weight.",
            author = null,
            topic = "human brain",
            savedAt = System.currentTimeMillis() - 86400000 // 1 day ago
        ),
        SavedItem(
            id = 2,
            category = FeatureCategory.QUOTE,
            content = "The only way to do great work is to love what you do.",
            author = "Steve Jobs",
            topic = "work",
            savedAt = System.currentTimeMillis() - 172800000 // 2 days ago
        ),
        SavedItem(
            id = 3,
            category = FeatureCategory.FACT,
            content = "Octopuses have three hearts and blue blood.",
            author = null,
            topic = "octopus",
            savedAt = System.currentTimeMillis() - 259200000 // 3 days ago
        ),
        SavedItem(
            id = 4,
            category = FeatureCategory.QUOTE,
            content = "In the middle of difficulty lies opportunity.",
            author = "Albert Einstein",
            topic = "opportunity",
            savedAt = System.currentTimeMillis() - 345600000 // 4 days ago
        ),
        SavedItem(
            id = 5,
            category = FeatureCategory.FACT,
            content = "A single bolt of lightning contains enough energy to toast 100,000 slices of bread.",
            author = null,
            topic = "lightning",
            savedAt = System.currentTimeMillis() - 432000000 // 5 days ago
        ),
        SavedItem(
            id = 6,
            category = FeatureCategory.QUOTE,
            content = "The best way to predict the future is to create it.",
            author = "Peter Drucker",
            topic = "future",
            savedAt = System.currentTimeMillis() - 518400000 // 6 days ago
        ),
        SavedItem(
            id = 7,
            category = FeatureCategory.FACT,
            content = "Honey never spoils. Archaeologists have found pots of honey in ancient Egyptian tombs that are over 3,000 years old and still perfectly edible.",
            author = null,
            topic = "honey",
            savedAt = System.currentTimeMillis() - 604800000 // 1 week ago
        ),
        SavedItem(
            id = 8,
            category = FeatureCategory.QUOTE,
            content = "Believe you can and you're halfway there.",
            author = "Theodore Roosevelt",
            topic = "belief",
            savedAt = System.currentTimeMillis() - 691200000 // 8 days ago
        ),
        SavedItem(
            id = 9,
            category = FeatureCategory.FACT,
            content = "A group of flamingos is called a 'flamboyance'.",
            author = null,
            topic = "flamingo",
            savedAt = System.currentTimeMillis() - 777600000 // 9 days ago
        ),
        SavedItem(
            id = 10,
            category = FeatureCategory.QUOTE,
            content = "The journey of a thousand miles begins with a single step.",
            author = "Lao Tzu",
            topic = "journey",
            savedAt = System.currentTimeMillis() - 864000000 // 10 days ago
        ),
        SavedItem(
            id = 11,
            category = FeatureCategory.FACT,
            content = "Bananas are berries, but strawberries aren't.",
            author = null,
            topic = "fruit",
            savedAt = System.currentTimeMillis() - 950400000 // 11 days ago
        ),
        SavedItem(
            id = 12,
            category = FeatureCategory.QUOTE,
            content = "Innovation distinguishes between a leader and a follower.",
            author = "Steve Jobs",
            topic = "innovation",
            savedAt = System.currentTimeMillis() - 1036800000 // 12 days ago
        ),
        SavedItem(
            id = 13,
            category = FeatureCategory.FACT,
            content = "The shortest war in history lasted only 38-45 minutes.",
            author = null,
            topic = "war",
            savedAt = System.currentTimeMillis() - 1123200000 // 13 days ago
        ),
        SavedItem(
            id = 14,
            category = FeatureCategory.QUOTE,
            content = "The only limit to our realization of tomorrow will be our doubts of today.",
            author = "Franklin D. Roosevelt",
            topic = "doubt",
            savedAt = System.currentTimeMillis() - 1209600000 // 14 days ago
        ),
        SavedItem(
            id = 15,
            category = FeatureCategory.FACT,
            content = "A shrimp's heart is in its head.",
            author = null,
            topic = "shrimp",
            savedAt = System.currentTimeMillis() - 1296000000 // 15 days ago
        )
    )
    return items
}
