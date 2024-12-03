package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vortex.mail_man_1.viewmodel.AuthState

@Composable
fun AuthScreen(
    authState: AuthState,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onGuestClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (authState) {
            AuthState.Loading -> {
                LoadingScreen()
            }
            is AuthState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Welcome ${authState.user?.displayName ?: "Guest"}")
                    Button(
                        onClick = onSignOutClick,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Sign Out")
                    }
                }
            }
            is AuthState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: ${authState.message}")
                    Button(
                        onClick = onSignInClick,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Retry Sign In")
                    }
                }
            }
            AuthState.Initial, AuthState.Unauthenticated -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Welcome to Mail_Man's app",
                            style = MaterialTheme.typography.displaySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onGuestClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Continue as Guest")
                        }
                        
                        Button(
                            onClick = onSignInClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Sign in with Google")
                        }
                    }
                }
            }
        }
    }
} 