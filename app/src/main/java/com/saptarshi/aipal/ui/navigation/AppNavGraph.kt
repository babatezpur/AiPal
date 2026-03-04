package com.saptarshi.aipal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.saptarshi.aipal.ui.auth.AuthScreen
import com.saptarshi.aipal.ui.auth.AuthViewModel


@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    val authViewModel : AuthViewModel = hiltViewModel()
    val token by authViewModel.tokenState.collectAsState(null)
    val startDestination = if(token != null) "main" else "auth"


    var hasCheckedToken by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(token) {
        if (!hasCheckedToken) {
            hasCheckedToken = true
        }
    }

    if (!hasCheckedToken) return  // blank screen while loading

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // Auth screens — will be added in the next step
        composable("auth") {
            AuthScreen(
                {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        // Main screen with bottom navigation
        composable("main") {
            MainScreen()
        }
    }
}