package com.saptarshi.aipal.data.repository

import com.saptarshi.aipal.data.local.db.dao.RecentActivityDao
import com.saptarshi.aipal.data.local.db.entity.RecentActivityEntity
import com.saptarshi.aipal.data.remote.api.FactsApi
import com.saptarshi.aipal.data.remote.api.QuotesApi
import com.saptarshi.aipal.data.remote.dto.FactsRequest
import com.saptarshi.aipal.data.remote.dto.QuotesRequest
import com.saptarshi.aipal.domain.model.Fact
import com.saptarshi.aipal.domain.model.Quote
import com.saptarshi.aipal.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class QuoteRepository @Inject constructor(
    private val quotesApi: QuotesApi,
    private val recentActivityDao: RecentActivityDao
) {


    suspend fun getFacts(topic: String, comment: String? = null) : Resource<List<Quote>> {
        return try{

            val response = quotesApi.getQuotes(QuotesRequest(topic, comment))

            if (response.isSuccessful) {
                val body = response.body()!!
                recentActivityDao.insertAndCleanup(
                    RecentActivityEntity(
                        topic = topic,
                        category = "quote",
                        timestamp = System.currentTimeMillis()
                    )
                )
                Resource.Success(body.quotes.map { Quote(it.text, author = it.author, topic) })
            } else {
                val errorMsg = if (response.code() == 429) "Daily limit reached" else "Failed to fetch quotes"
                Resource.Error(errorMsg)
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")

        }
    }

}
