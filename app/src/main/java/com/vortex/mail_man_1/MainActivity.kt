package com.vortex.mail_man_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.vortex.mail_man_1.ui.theme.Mail_man_1Theme
import com.vortex.mail_man_1.viewmodel.AuthState
import com.vortex.mail_man_1.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            lifecycleScope.launch {
                viewModel.handleGoogleSignInResult(result.data)
            }
        }

        setContent {
            Mail_man_1Theme {
                val authState by viewModel.authState.collectAsState()
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen(
                        authState = authState,
                        onSignInClick = { 
                            val signInIntent = viewModel.getGoogleSignInClient(this).signInIntent
                            googleSignInLauncher.launch(signInIntent)
                        },
                        onSignOutClick = {
                            viewModel.signOut(this)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AuthScreen(
    authState: AuthState,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (authState) {
            is AuthState.Initial -> {
                Button(onClick = onSignInClick) {
                    Text("Sign in with Google")
                }
            }
            is AuthState.Success -> {
                Text("Welcome ${authState.user?.displayName ?: "User"}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onSignOutClick) {
                    Text("Sign Out")
                }
            }
            is AuthState.Error -> {
                Text("Error: ${authState.message}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onSignInClick) {
                    Text("Retry")
                }
            }
        }
    }
}