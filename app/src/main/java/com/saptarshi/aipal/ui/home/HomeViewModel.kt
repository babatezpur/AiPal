package com.saptarshi.aipal.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saptarshi.aipal.data.repository.HomeRepository
import com.saptarshi.aipal.domain.model.RecentActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    val recentActivities : StateFlow<List<RecentActivity>> =
        homeRepository.getRecentActivities()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}