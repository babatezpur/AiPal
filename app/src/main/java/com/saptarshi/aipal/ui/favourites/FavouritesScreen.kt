package com.saptarshi.aipal.ui.favourites

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FavouritesScreen(
    viewModel : FavouritesViewModel = hiltViewModel()
) {

 FavouriteScreenContent()

}

@Composable
fun FavouriteScreenContent() {

    LazyColumn(
        content = {

        }
    )
}

@Preview
@Composable
fun FavouriteScreenPreview() {
    FavouriteScreenContent()
}