package com.saptarshi.aipal.data.repository

import com.saptarshi.aipal.data.local.db.dao.RecentActivityDao
import com.saptarshi.aipal.domain.model.RecentActivity
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val recentActivityDao: RecentActivityDao
) {
    fun getRecentActivities() = recentActivityDao.getRecentActivities().map { entities ->
        entities.map { RecentActivity(it.id, it.topic, it.category, it.timestamp) }
    }

}