package com.saptarshi.aipal.data.repository

import com.saptarshi.aipal.data.local.db.dao.RecentActivityDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val recentActivityDao: RecentActivityDao
) {
    fun getRecentActivity() = recentActivityDao.getRecentActivities()

}