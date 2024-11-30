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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavOptions
import com.vortex.mail_man_1.navigation.NavDestination
import com.vortex.mail_man_1.screens.*
import com.vortex.mail_man_1.ui.theme.Mail_man_1Theme
import com.vortex.mail_man_1.viewmodel.AuthState
import com.vortex.mail_man_1.viewmodel.AuthViewModel
import com.vortex.mail_man_1.components.BottomNavBar
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
                    MainScreen(authState)
                }
            }
        }
    }

    @Composable
    fun MainScreen(authState: AuthState) {
        when (authState) {
            is AuthState.Success -> {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavBar(
                            currentRoute = navController.currentDestination?.route ?: NavDestination.Home.route,
                            onNavigate = { destination ->
                                navController.navigate(destination.route) {
                                    popUpTo(NavDestination.Home.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = NavDestination.Home.route,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(NavDestination.Home.route) { HomeScreen() }
                        composable(NavDestination.Pomodoro.route) { PomodoroScreen() }
                        composable(NavDestination.CreateNote.route) { CreateNoteScreen() }
                        composable(NavDestination.KanbanBoard.route) { KanbanBoardScreen() }
                        composable(NavDestination.Settings.route) { SettingsScreen() }
                    }
                }
            }
            is AuthState.Initial -> {
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
            is AuthState.Error -> {
                Text("Error: ${authState.message}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { 
                    val signInIntent = viewModel.getGoogleSignInClient(this).signInIntent
                    googleSignInLauncher.launch(signInIntent)
                }) {
                    Text("Retry")
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