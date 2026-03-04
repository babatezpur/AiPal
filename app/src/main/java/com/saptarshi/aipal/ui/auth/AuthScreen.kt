package com.saptarshi.aipal.ui.auth

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saptarshi.aipal.utils.Resource


@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {

    var isSignupMode by rememberSaveable { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()
    val signupState by viewModel.signupState.collectAsState()

    val currentState = if (isSignupMode) signupState else loginState
    LaunchedEffect(currentState) {
        if (currentState is Resource.Success) {
            onAuthSuccess()
        }
    }


    AuthScreenContent(
        isSignupMode = isSignupMode,
        onSignupModeToggle = {
            isSignupMode = !isSignupMode
            viewModel.resetLoginState()
            viewModel.resetSignupState()
        },
        onLoginClick = { email, password ->
            viewModel.login(email, password)
        },
        onSignupClick = { username, email, password ->
            viewModel.register(username, email, password)
        },
        isLoading = currentState is Resource.Loading,
        errorMessage = (currentState as? Resource.Error)?.message
    )
}

@Composable
fun AuthScreenContent(
    isSignupMode: Boolean,
    onSignupModeToggle: () -> Unit,
    onLoginClick: (String, String) -> Unit,
    onSignupClick: (String, String, String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val pageTitle = if (isSignupMode) "Sign Up" else "Login"
    val buttonText = if (isSignupMode) "Sign Up" else "Login"

    Log.d("MainScreen", "SignupMode is : $isSignupMode")

    val headerText = if (isSignupMode) "Hello!\nPlease create your account" else "Welcome back! \nPlease login to continue"




    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
    ) {

        // 🔹 TOP FORM CONTENT
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
        ) {

            Text(
                text = headerText,
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary,
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
            )

            if (isSignupMode) {
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Username") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "Username")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )
            }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password")
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
        }

        // 🔹 BOTTOM GROUP (Button + Link together)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedButton(
                onClick = {
                    if (isSignupMode) {
                        onSignupClick(userName, email, password)
                    } else {
                        onLoginClick(email, password)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .width(2.dp)
                    )
                }
                else Text(
                    text = buttonText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }

            Text(
                text = if (isSignupMode)
                    "Already have an account? Login"
                else
                    "Don't have an account? Sign Up",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { onSignupModeToggle() },
                color = MaterialTheme.colorScheme.primary
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
//        if (isSignupMode) {
//            IconButton(
//                onClick = { onSignupModeToggle() },
//                modifier = Modifier
//                    .align(Alignment.TopStart)
//                    .padding(top = (.dp, start = 0.dp)
//            ) {
//                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//            }
//        }
    }

    if (isSignupMode) {
        BackHandler { onSignupModeToggle() }
    }

}



@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreenContent(
        isSignupMode = false,
        onSignupModeToggle = {},
        onLoginClick = { _, _ -> },
        onSignupClick = { _, _, _ -> },
        isLoading = false,
    )
}