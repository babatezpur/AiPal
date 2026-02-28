package com.saptarshi.aipal.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SaveFavouriteRequest(
    val category: String,
    val content: String,
    val author: String? = null,
    val topic: String
)

data class FavouriteDto(
    val id: Int,
    val category: String,
    val content: String,
    val author: String?,
    val topic: String,
    @SerializedName("created_at")
    val createdAt: String
)

