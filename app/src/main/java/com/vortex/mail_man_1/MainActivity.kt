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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vortex.mail_man_1.navigation.NavDestination
import com.vortex.mail_man_1.screens.*
import com.vortex.mail_man_1.ui.theme.Mail_man_1Theme
import com.vortex.mail_man_1.viewmodel.AuthState
import com.vortex.mail_man_1.viewmodel.AuthViewModel
import com.vortex.mail_man_1.components.BottomNavBar
import kotlinx.coroutines.launch
import com.vortex.mail_man_1.viewmodel.NotesViewModel

/**
 * Main activity class that handles the app's entry point and authentication flow
 */
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val notesViewModel: NotesViewModel by viewModels()
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Google Sign-In launcher
        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            lifecycleScope.launch {
                authViewModel.handleGoogleSignInResult(result.data)
            }
        }

        setContent {
            Mail_man_1Theme {
                val authState by authViewModel.authState.collectAsState()
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (authState) {
                        is AuthState.Success -> {
                            val navController = rememberNavController()
                            
                            // Add state tracking for current route
                            val currentRoute by navController.currentBackStackEntryAsState()
                            println("Current BackStack Route: ${currentRoute?.destination?.route}") // Debug log

                            Scaffold(
                                bottomBar = {
                                    BottomNavBar(
                                        currentRoute = currentRoute?.destination?.route 
                                            ?: NavDestination.Home.route,
                                        onNavigate = { destination ->
                                            println("Navigating to: ${destination.route}") // Debug log
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
                                    composable(NavDestination.CreateNote.route) { 
                                        CreateNoteScreen(
                                            onNavigateBack = { navController.popBackStack() },
                                            viewModel = notesViewModel
                                        )
                                    }
                                    composable(NavDestination.KanbanBoard.route) { KanbanBoardScreen() }
                                    composable(NavDestination.Settings.route) { 
                                        SettingsScreen(
                                            onNavigateBack = { navController.popBackStack() },
                                            onSignOut = { authViewModel.signOut(this@MainActivity) }
                                        )
                                    }
                                }
                            }
                        }
                        else -> {
                            AuthScreen(
                                authState = authState,
                                onSignInClick = { 
                                    val signInIntent = authViewModel.getGoogleSignInClient(this).signInIntent
                                    googleSignInLauncher.launch(signInIntent)
                                },
                                onSignOutClick = {
                                    authViewModel.signOut(this)
                                }
                            )
                        }
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
                AuthState.Loading -> {
                    CircularProgressIndicator()
                }
                AuthState.Unauthenticated -> {
                    Button(onClick = onSignInClick) {
                        Text("Sign in with Google")
                    }
                }
            }
        }
    }
}