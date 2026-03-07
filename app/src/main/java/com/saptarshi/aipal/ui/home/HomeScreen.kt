package com.saptarshi.aipal.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.saptarshi.aipal.data.local.db.entity.RecentActivityEntity
import com.saptarshi.aipal.domain.model.FeatureCategory
import com.saptarshi.aipal.domain.model.RecentActivity
import com.saptarshi.aipal.ui.components.RecentActivityTile

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen (
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreenContent(viewModel)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(viewModel: HomeViewModel) {

    val recentActivities by viewModel.recentActivities.collectAsState()
    val listItems = mutableListOf<RecentActivity>(
        RecentActivity(1, "Topic 1", FeatureCategory.FACT, 1234567890),
        RecentActivity(2, "Topic 2", FeatureCategory.QUOTE, 1234567890),
        RecentActivity(3, "Topic 3", FeatureCategory.FACT, 1234567890),

        RecentActivity(4, "Topic 4", FeatureCategory.QUOTE, 1234567890),
        RecentActivity(5, "Topic 5", FeatureCategory.FACT, 1234567890),
        RecentActivity(6, "Topic 6", FeatureCategory.QUOTE, 1234567890),
        RecentActivity(7, "Topic 7", FeatureCategory.QUOTE, 1234567890),
        RecentActivity(8, "Topic 8", FeatureCategory.FACT, 1234567890),
        RecentActivity(9, "Topic 9", FeatureCategory.QUOTE, 1234567890),
    )


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text("Home")
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(top=10.dp, start=10.dp),
            // provide some gap between each element
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            Text(
                text = "RECENTS",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 5.dp)

            )
            listItems.forEach {
                RecentActivityTile(it)
            }
        }

    }
}
