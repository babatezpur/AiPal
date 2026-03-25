package com.saptarshi.aipal.ui.favourites

import androidx.lifecycle.ViewModel
import com.saptarshi.aipal.data.repository.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    favouritesRepository: FavouritesRepository
) : ViewModel() {
}