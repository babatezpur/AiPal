package com.saptarshi.aipal.ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saptarshi.aipal.data.repository.FavouritesRepository
import com.saptarshi.aipal.domain.model.SavedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import com.saptarshi.aipal.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    val favourites: StateFlow<List<SavedItem>> = favouritesRepository.getCachedFavourites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            favouritesRepository.refreshFavourites()
        }
    }

    fun removeFromFavourites(item: SavedItem) {
        viewModelScope.launch {
            val result = favouritesRepository.deleteFavourite(item.id)
            if (result is Resource.Error) {
                _errorEvent.emit(result.message ?: "Failed to remove favourite")
            }
        }
    }

}