package com.saptarshi.aipal.data.repository

import com.saptarshi.aipal.data.local.datastore.TokenManager
import com.saptarshi.aipal.data.local.db.dao.RecentActivityDao
import com.saptarshi.aipal.data.local.db.dao.UserProfileDao
import com.saptarshi.aipal.data.local.db.entity.RecentActivityEntity
import com.saptarshi.aipal.data.remote.api.AuthApi
import com.saptarshi.aipal.data.remote.api.FactsApi
import com.saptarshi.aipal.data.remote.dto.FactsRequest
import com.saptarshi.aipal.data.remote.dto.FactsResponse
import com.saptarshi.aipal.domain.model.Fact
import com.saptarshi.aipal.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FactRepository @Inject constructor(
    private val factsApi: FactsApi,
    private val recentActivityDao: RecentActivityDao
) {


    suspend fun getFacts(topic: String, comment: String? = null) : Resource<List<Fact>> {
        return try{

            val response = factsApi.getFacts(FactsRequest(topic, comment))

            if (response.isSuccessful) {
                val body = response.body()!!
                recentActivityDao.insertAndCleanup(
                    RecentActivityEntity(
                        topic = topic,
                        category = "fact",
                        timestamp = System.currentTimeMillis()
                    )
                )
                Resource.Success(body.facts.map { Fact(it, topic) })
            } else {
                val errorMsg = if (response.code() == 429) "Daily limit reached" else "Failed to fetch facts"
                Resource.Error(errorMsg)
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")

        }
    }

}
