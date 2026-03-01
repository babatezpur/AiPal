package com.saptarshi.aipal.data.remote.api

import com.saptarshi.aipal.data.remote.dto.FactsRequest
import com.saptarshi.aipal.data.remote.dto.FactsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FactsApi {

    @POST("facts/")
    suspend fun getFacts(@Body request: FactsRequest): Response<FactsResponse>
}
