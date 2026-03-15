package com.saptarshi.aipal.ui.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.saptarshi.aipal.domain.model.Conversation
import com.saptarshi.aipal.ui.components.ChatTile
import kotlin.collections.forEach

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListScreen (
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val recentChats by viewModel.recentChats.collectAsState()

    ChatListScreenContent(
        recentChats,
        onChatClick = { chat ->
            navController.navigate("search?topic=${chat.id}")
        },
        onFabClick = {
            navController.navigate("search")
        }
    )
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreenContent(
    recentChats : List<Conversation>,
    onChatClick : (Conversation) -> Unit,
    onFabClick : () -> Unit
) {



    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            title = {
                Text("Chats")
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

        Box(modifier = Modifier.weight(1f)) {

            if (recentChats.isEmpty()) {
                // Show inside the LazyColumn or as a Box
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,

                    ) {
                    Text(
                        text = "No recent activity yet.\nTap + to search for facts or quotes!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 10.dp, start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {


                if(recentChats.isNotEmpty())
                    Text(
                        text = "RECENTS",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                    )
                recentChats.forEach {
                     ChatTile(it, { onChatClick(it) })
                }
            }

            FloatingActionButton(
                onClick = { onFabClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(25.dp)
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    }
}

// PreView
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ChatListScreenPreview() {
    val recentChats = listOf(
        Conversation(
            id = 1,
            title = "Chat about Space",
            messageCount = 3,
            createdAt = System.currentTimeMillis()
        ),
        Conversation(
            id = 2,
            title = "Chat about Technology",
            messageCount = 5,
            createdAt = System.currentTimeMillis() - 3600000
        ),
        Conversation(
            id = 3,
            title = "Chat about History",
            messageCount = 2,
            createdAt = System.currentTimeMillis() - 7200000
        )
    )

    ChatListScreenContent(
        recentChats = recentChats,
        onChatClick = {},
        onFabClick = {}
    )
}
