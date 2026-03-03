package com.saptarshi.aipal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController


@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    // TODO: Once AuthViewModel is built, we'll check JWT here:

    // val authViewModel : AuthViewModel = hiltViewModel()
    // val token = authViewModel.tokenState.collectAsState()
    // val startDestination = if(token != null) 'home' else 'login'

    NavHost(
        navController = navController,
        startDestination = "main",
    ) {
        // Auth screens — will be added in the next step
        navigation(startDestination = "login", route = "auth") {
            composable("login") { /* LoginScreen */ }
            composable("signup") { /* SignupScreen */ }
        }

        // Main screen with bottom navigation
        composable("main") {
            MainScreen()
        }
    }
}