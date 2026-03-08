package com.saptarshi.aipal.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saptarshi.aipal.data.repository.FactRepository
import com.saptarshi.aipal.data.repository.FavouritesRepository
import com.saptarshi.aipal.data.repository.QuoteRepository
import com.saptarshi.aipal.domain.model.FeatureCategory
import com.saptarshi.aipal.domain.model.SearchResult
import com.saptarshi.aipal.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val factsRepository: FactRepository,
    private val quotesRepository: QuoteRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    // Which mode is selected — facts or quotes
    private val _selectedCategory = MutableStateFlow(FeatureCategory.FACT)
    val selectedCategory: StateFlow<FeatureCategory> = _selectedCategory.asStateFlow()

    // Search results
    private val _searchState = MutableStateFlow<Resource<List<SearchResult>>?>(null)
    val searchState: StateFlow<Resource<List<SearchResult>>?> = _searchState.asStateFlow()

    // Track which results have been saved (by index)
    private val _savedIndices = MutableStateFlow<Set<Int>>(emptySet())
    val savedIndices: StateFlow<Set<Int>> = _savedIndices.asStateFlow()

    fun toggleCategory(category: FeatureCategory) {
        _selectedCategory.value = category
        // Clear previous results when switching category
        _searchState.value = null
        _savedIndices.value = emptySet()
    }

    fun search(topic: String, comment: String?) {
        if (topic.isBlank()) return

        viewModelScope.launch {
            _searchState.value = Resource.Loading
            _savedIndices.value = emptySet()

            when (_selectedCategory.value) {
                FeatureCategory.FACT -> {
                    val result = factsRepository.getFacts(topic, comment)
                    _searchState.value = when (result) {
                        is Resource.Success -> {
                            val searchResults = result.data?.map { fact ->
                                SearchResult(
                                    content = fact.content,
                                    author = null,
                                    topic = topic,
                                    category = FeatureCategory.FACT
                                )
                            } ?: emptyList()
                            Resource.Success(searchResults)
                        }
                        is Resource.Error -> Resource.Error(result.message ?: "Unknown error")
                        is Resource.Loading -> Resource.Loading
                    }
                }

                FeatureCategory.QUOTE -> {
                    val result = quotesRepository.getQuotes(topic, comment)
                    _searchState.value = when (result) {
                        is Resource.Success -> {
                            val searchResults = result.data?.map { quote ->
                                SearchResult(
                                    content = quote.content,
                                    author = quote.author,
                                    topic = topic,
                                    category = FeatureCategory.QUOTE
                                )
                            } ?: emptyList()
                            Resource.Success(searchResults)
                        }
                        is Resource.Error -> Resource.Error(result.message ?: "Unknown error")
                        is Resource.Loading -> Resource.Loading
                    }
                }
            }
        }
    }

    fun saveFavourite(result: SearchResult, index: Int) {
        viewModelScope.launch {
            val response = favouritesRepository.saveFavourite(
                category = if (result.category == FeatureCategory.FACT) "fact" else "quote",
                content = result.content,
                author = result.author,
                topic = result.topic
            )

            if (response is Resource.Success) {
                // Add this index to saved set so the heart fills in
                _savedIndices.value += index
            }
        }
    }
}