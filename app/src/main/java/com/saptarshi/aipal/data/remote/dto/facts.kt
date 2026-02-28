package com.saptarshi.aipal.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FactsRequest(
    val topic: String,
    val comment: String? = null
)

data class FactsResponse(
    val message: String,
    val facts: List<String>,
    @SerializedName("remaining_requests")
    val remainingRequests: Int
)


