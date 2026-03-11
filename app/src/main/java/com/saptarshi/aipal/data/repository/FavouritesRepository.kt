package com.saptarshi.aipal.data.repository

import com.saptarshi.aipal.data.local.db.dao.SavedItemDao
import com.saptarshi.aipal.data.local.db.entity.SavedItemEntity
import com.saptarshi.aipal.data.remote.api.FavouritesApi
import com.saptarshi.aipal.data.remote.dto.SaveFavouriteRequest
import com.saptarshi.aipal.domain.model.SavedItem
import com.saptarshi.aipal.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritesRepository @Inject constructor(
    private val favouritesApi: FavouritesApi,
    private val savedItemDao: SavedItemDao
) {

    fun getCachedFavourites(): Flow<List<SavedItem>> {
        return savedItemDao.getAllSavedItems().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getCachedFavouritesByCategory(category: String): Flow<List<SavedItem>> {
        return savedItemDao.getSavedItemsByCategory(category).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun refreshFavourites(category: String? = null): Resource<List<SavedItem>> {
        return try {
            val response = favouritesApi.getFavourites(category)
            if (response.isSuccessful) {
                val items = response.body()!!
                val entities = items.map {
                    SavedItemEntity(it.id, it.category, it.content, it.author, it.topic, System.currentTimeMillis())
                }
                savedItemDao.deleteAllSavedItems()
                savedItemDao.insertSavedItems(entities)
                Resource.Success(items.map { SavedItem(it.id, it.category, it.content, it.author, it.topic, System.currentTimeMillis()) })
            } else {
                Resource.Error("Failed to fetch favourites")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun saveFavourite(category: String, content: String, author: String?, topic: String): Resource<SavedItem> {
        return try {
            val response = favouritesApi.saveFavourite(
                SaveFavouriteRequest(
                    category,
                    content,
                    author,
                    topic
                )
            )
            if (response.isSuccessful) {
                val item = response.body()!!.favourite
                savedItemDao.insertSavedItem(
                    SavedItemEntity(item.id, item.category, item.content, item.author, item.topic, System.currentTimeMillis())
                )
                Resource.Success(SavedItem(item.id, item.category, item.content, item.author, item.topic, System.currentTimeMillis()))
            } else {
                Resource.Error("Failed to save favourite")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun deleteFavourite(id: Int): Resource<Unit> {
        return try {
            val response = favouritesApi.deleteFavourite(id)
            if (response.isSuccessful) {
                savedItemDao.deleteSavedItem(id)
                Resource.Success(Unit)
            } else {
                Resource.Error("Failed to delete favourite")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    private fun SavedItemEntity.toDomainModel() =
        SavedItem(id, category, content, author, topic, savedAt)
}