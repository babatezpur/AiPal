package com.saptarshi.aipal.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.saptarshi.aipal.ui.auth.AuthScreen
import com.saptarshi.aipal.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.map

// Tri-state: distinguish "still loading" from "no token"
private enum class AuthState { LOADING, LOGGED_IN, LOGGED_OUT }

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()

    // Map token flow to tri-state.
    // collectAsState initial = LOADING; once DataStore emits, it becomes LOGGED_IN or LOGGED_OUT.
    val authState by authViewModel.tokenState
        .map { token -> if (token != null) AuthState.LOGGED_IN else AuthState.LOGGED_OUT }
        .collectAsState(AuthState.LOADING)

    // Show nothing while DataStore is still reading
    if (authState == AuthState.LOADING) return

    val startDestination = if (authState == AuthState.LOGGED_IN) "main" else "auth"

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable("auth") {
            AuthScreen(
                {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreen(
                onLogout = {
                    navController.navigate("auth") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}
