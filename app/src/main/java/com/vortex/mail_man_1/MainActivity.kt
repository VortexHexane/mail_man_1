package com.vortex.mail_man_1

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vortex.mail_man_1.navigation.NavDestination
import com.vortex.mail_man_1.screens.*
import com.vortex.mail_man_1.ui.theme.Mail_man_1Theme
import com.vortex.mail_man_1.viewmodel.AuthState
import com.vortex.mail_man_1.viewmodel.AuthViewModel
import com.vortex.mail_man_1.components.BottomNavBar
import kotlinx.coroutines.launch
import com.vortex.mail_man_1.viewmodel.NotesViewModel
import com.vortex.mail_man_1.viewmodel.KanbanViewModel

/**
 * Main activity class that handles the app's entry point and authentication flow
 */
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val notesViewModel: NotesViewModel by viewModels()
    private val kanbanViewModel: KanbanViewModel by viewModels()
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable drawing system bar backgrounds
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // Set the status bar color to black
        window.statusBarColor = android.graphics.Color.BLACK

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
                            val username = (authState as AuthState.Success).user?.displayName?.split(" ")?.first() ?: "Guest"

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
                                    composable(NavDestination.Home.route) { HomeScreen(username, kanbanViewModel) }
                                    composable(NavDestination.Pomodoro.route) { PomodoroScreen() }
                                    composable(NavDestination.CreateNote.route) { 
                                        CreateNoteScreen(
                                            onNavigateBack = { navController.popBackStack() },
                                            viewModel = notesViewModel
                                        )
                                    }
                                    composable(NavDestination.KanbanBoard.route) { KanbanBoardScreen(viewModel = kanbanViewModel) }
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
                                    authViewModel.startSignIn() 
                                    val signInIntent = authViewModel.getGoogleSignInClient(this).signInIntent
                                    googleSignInLauncher.launch(signInIntent)
                                },
                                onSignOutClick = {
                                    authViewModel.signOut(this)
                                },
                                onGuestClick = {
                                    authViewModel.continueAsGuest()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}