package com.saptarshi.aipal.data.remote.api

import com.saptarshi.aipal.data.remote.dto.QuotesRequest
import com.saptarshi.aipal.data.remote.dto.QuotesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface QuotesApi {

    @POST("/quotes/")
    suspend fun getQuotes(@Body request: QuotesRequest): Response<QuotesResponse>
}