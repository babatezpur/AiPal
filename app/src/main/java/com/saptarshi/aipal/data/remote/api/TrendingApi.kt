package com.saptarshi.aipal.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class TrendingTopic(
    val topic: String,
    val count: Int
)

data class TrendingResponse(
    val trending: List<TrendingTopic>
)

interface TrendingApi {

    @GET("trending/")
    suspend fun getTrending(@Query("feature") feature: String? = null): Response<TrendingResponse>
}