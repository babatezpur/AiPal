package com.saptarshi.aipal.data.remote.api

import com.saptarshi.aipal.data.remote.dto.FavouriteDto
import com.saptarshi.aipal.data.remote.dto.SaveFavouriteRequest
import retrofit2.Response
import retrofit2.http.*

interface FavouritesApi {

    @POST("favourites/")
    suspend fun saveFavourite(@Body request: SaveFavouriteRequest): Response<FavouriteDto>

    @GET("favourites/")
    suspend fun getFavourites(@Query("category") category: String? = null): Response<List<FavouriteDto>>

    @DELETE("favourites/{id}")
    suspend fun deleteFavourite(@Path("id") id: Int): Response<Unit>
}