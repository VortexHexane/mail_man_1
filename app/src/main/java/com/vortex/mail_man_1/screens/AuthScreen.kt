package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vortex.mail_man_1.viewmodel.AuthState

@Composable
fun AuthScreen(
    authState: AuthState,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    when (authState) {
        AuthState.Loading -> {
            LoadingScreen()
        }
        is AuthState.Success -> {
            Text("Welcome ${authState.user?.displayName}")
            Button(
                onClick = onSignOutClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Sign Out")
            }
        }
        is AuthState.Error -> {
            Text("Error: ${authState.message}")
            Button(
                onClick = onSignInClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Retry Sign In")
            }
        }
        AuthState.Initial -> {
            Button(onClick = onSignInClick) {
                Text("Sign In with Google")
            }
        }
        AuthState.Unauthenticated -> {
            Button(onClick = onSignInClick) {
                Text("Sign In with Google")
            }
        }
    }
} 