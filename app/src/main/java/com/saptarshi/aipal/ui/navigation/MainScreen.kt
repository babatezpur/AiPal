package com.saptarshi.aipal.ui.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saptarshi.aipal.ui.chat.ChatListScreen
import com.saptarshi.aipal.ui.chat.ChatScreen
import com.saptarshi.aipal.ui.home.HomeScreen
import com.saptarshi.aipal.ui.home.SearchScreen
import com.saptarshi.aipal.ui.theme.AiPalTheme


enum class BottomNavTab(
    val route : String,
    val label : String,
    val icon : ImageVector,
) {
    HOME("home", "Home", Icons.Default.Home),
    CHATS("chat_list", "Chats", Icons.Default.Email),
    PROFILE("profile", "Profile", Icons.Default.Person)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar() {
                BottomNavTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                // Restore previously saved state when re-selecting a tab
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )

                }

            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavTab.HOME.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // ---- Home Tab ----
            composable(BottomNavTab.HOME.route) {
                HomeScreen(navController)
            }

            // Search screen for facts/quotes (launched from Home's FAB)
            // topic and category are optional — empty when from FAB, pre-filled when from recent activity
            composable(
                route = "search?topic={topic}&category={category}",
                arguments = listOf(
                    navArgument("topic") { defaultValue = "" },
                    navArgument("category") { defaultValue = "" }
                )
            ) {
                val topic = it.arguments?.getString("topic") ?: ""
                val category = it.arguments?.getString("category") ?: ""
                SearchScreen(topic = topic, category = category, navController = navController)
            }

            // ---- Chats Tab ----
            composable(BottomNavTab.CHATS.route) {
                ChatListScreen(navController)
            }

            // Individual chat screen
            // "chat/new" = new conversation, "chat/{id}" = existing conversation
            composable(
                "chat/{conversationId}",
                arguments = listOf(
                    navArgument("conversationId") { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                val rawId = navBackStackEntry.arguments?.getString("conversationId")
                val conversationId = if (rawId == "new") null else rawId?.toIntOrNull()
                ChatScreen(
                    conversationId = conversationId,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // ---- Profile Tab ----
            composable(BottomNavTab.PROFILE.route) {
                PlaceholderScreen("Profile")
            }

            // Favourites screen (launched from Profile)
            composable("favourites") {
                PlaceholderScreen("Favourites")
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AiPalTheme {
        MainScreen()
    }
}

// Temporary placeholder screen — will be replaced with actual screens later.
@Composable
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}