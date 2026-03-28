package com.saptarshi.aipal.ui.favourites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.saptarshi.aipal.ui.theme.AiPalTheme
import kotlin.math.absoluteValue

@Composable
fun FavouritesScreen(
    viewModel : FavouritesViewModel = hiltViewModel()
) {

 FavouriteScreenContent()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreenContent() {

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
                emptyList()
            )
        } else {
            DataListView(
                data = emptyList()
            )
        }
    }
}

@Composable
fun DataListView(
    data: List<SavedItem>
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
                    text = "Saved Item #${data[index].topic}",
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
    data: List<SavedItem>
) {
    val pagerState = rememberPagerState(pageCount = { 10 })

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 84.dp), // shows side items
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

        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    alpha = lerp(0.5f, 1f, 1f - pageOffset)
                }
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Blue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = page.toString(),
                color = Color.White,
                fontSize = 32.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavouriteScreenPreview() {
    AiPalTheme {
        FavouriteScreenContent()
    }
}