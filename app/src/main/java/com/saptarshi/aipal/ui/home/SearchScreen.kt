package com.saptarshi.aipal.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saptarshi.aipal.domain.model.FeatureCategory
import com.saptarshi.aipal.domain.model.SearchResult
import com.saptarshi.aipal.ui.components.ResultCard
import com.saptarshi.aipal.utils.Resource

@Composable
fun SearchScreen(
    topic: String = "",
    category: String = "",
    onBackClick: () -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchState by viewModel.searchState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val savedIndices by viewModel.savedIndices.collectAsState()

    // Pre-fill category from navigation argument
    if (category.isNotEmpty()) {
        val preselected = if (category == "FACT") FeatureCategory.FACT else FeatureCategory.QUOTE
        viewModel.toggleCategory(preselected)
    }

    SearchScreenContent(
        initialTopic = topic,
        selectedCategory = selectedCategory,
        searchState = searchState,
        savedIndices = savedIndices,
        onCategoryToggle = { viewModel.toggleCategory(it) },
        onSearch = { searchTopic, comment -> viewModel.search(searchTopic, comment) },
        onSaveClick = { result, index -> viewModel.saveFavourite(result, index) },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    initialTopic: String = "",
    selectedCategory: FeatureCategory = FeatureCategory.FACT,
    searchState: Resource<List<SearchResult>>? = null,
    savedIndices: Set<Int> = emptySet(),
    onCategoryToggle: (FeatureCategory) -> Unit = {},
    onSearch: (String, String?) -> Unit = { _, _ -> },
    onSaveClick: (SearchResult, Int) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {}
) {
    var topic by rememberSaveable { mutableStateOf(initialTopic) }
    var comment by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        // Top bar with back arrow
        TopAppBar(
            title = { Text("Search") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Category toggle — Facts / Quotes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = selectedCategory == FeatureCategory.FACT,
                    onClick = { onCategoryToggle(FeatureCategory.FACT) },
                    label = { Text("Facts") },
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = selectedCategory == FeatureCategory.QUOTE,
                    onClick = { onCategoryToggle(FeatureCategory.QUOTE) },
                    label = { Text("Quotes") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Topic input
            OutlinedTextField(
                value = topic,
                onValueChange = { topic = it },
                label = { Text("Topic") },
                placeholder = { Text("e.g. black holes, perseverance") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Comment input (optional)
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Instructions (optional)") },
                placeholder = { Text("e.g. make it beginner friendly") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Generate button
            Button(
                onClick = {
                    onSearch(topic, comment.ifBlank { null })
                },
                enabled = topic.isNotBlank() && searchState !is Resource.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (searchState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Generate",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            // Results section
            when (searchState) {
                is Resource.Success -> {
                    val results = searchState.data ?: emptyList()

                    Text(
                        text = "Results",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    results.forEachIndexed { index, result ->
                        ResultCard(
                            result = result,
                            isSaved = savedIndices.contains(index),
                            onSaveClick = { onSaveClick(result, index) }
                        )
                    }
                }

                is Resource.Error -> {
                    Text(
                        text = searchState.message ?: "Something went wrong",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }

                else -> {
                    // null or Loading with no previous results — show nothing extra
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenEmptyPreview() {
    SearchScreenContent()
}

@Preview(showBackground = true)
@Composable
fun SearchScreenWithResultsPreview() {
    SearchScreenContent(
        initialTopic = "black holes",
        selectedCategory = FeatureCategory.FACT,
        searchState = Resource.Success(
            listOf(
                SearchResult("A black hole forms when a massive star collapses.", null, "black holes", FeatureCategory.FACT),
                SearchResult("Nothing can escape a black hole's event horizon.", null, "black holes", FeatureCategory.FACT),
                SearchResult("Time slows down near a black hole.", null, "black holes", FeatureCategory.FACT),
                SearchResult("Time slows down near a black hole.", null, "black holes", FeatureCategory.FACT),
                SearchResult("Time slows down near a black hole.", null, "black holes", FeatureCategory.FACT),
            )
        ),
        savedIndices = setOf(1)
    )
}