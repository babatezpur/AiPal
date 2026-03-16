package com.saptarshi.aipal.data.remote.api

import com.saptarshi.aipal.data.remote.dto.ConversationDetailResponse
import com.saptarshi.aipal.data.remote.dto.ConversationListResponse
import com.saptarshi.aipal.data.remote.dto.ConversationStartResponse
import com.saptarshi.aipal.data.remote.dto.SendMessageRequest
import com.saptarshi.aipal.data.remote.dto.SendMessageResponse
import com.saptarshi.aipal.data.remote.dto.StartConversationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ConversationApi {

    @POST("conversation/start")
    suspend fun startConversation(
        @Body request: StartConversationRequest
    ): Response<ConversationStartResponse>

    @POST("conversation/message")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<SendMessageResponse>

    @GET("conversation/conversations")
    suspend fun getConversations(): Response<ConversationListResponse>

    @GET("conversation/conversations/{id}")
    suspend fun getConversation(@Path("id") id: Int): Response<ConversationDetailResponse>
}