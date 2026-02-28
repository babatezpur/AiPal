package com.saptarshi.aipal.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QuotesRequest(
    val topic: String,
    val comment: String? = null
)

data class QuoteDto(
    val text: String,
    val author: String
)

data class QuotesResponse(
    val message: String,
    val quotes: List<QuoteDto>,
    @SerializedName("remaining_requests")
    val remainingRequests: Int
)
