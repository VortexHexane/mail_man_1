package com.vortex.mail_man_1.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vortex.mail_man_1.screens.*
import com.vortex.mail_man_1.viewmodel.AuthState
import com.vortex.mail_man_1.viewmodel.NotesViewModel
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.vortex.mail_man_1.components.BottomNavBar

/**
 * Main navigation component for the app
 */
@Composable
fun AppNavigation(
    authState: AuthState,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit,
    notesViewModel: NotesViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            if (authState is AuthState.Success) {
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
        }
    ) { padding ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(padding),
            startDestination = when (authState) {
                is AuthState.Success -> NavDestination.Home.route
                else -> NavDestination.Auth.route
            }
        ) {
            composable(NavDestination.Auth.route) {
                AuthScreen(
                    authState = authState,
                    onSignInClick = onSignInClick,
                    onSignOutClick = onSignOutClick
                )
            }
            
            // Protected routes
            composable(NavDestination.Home.route) {
                HomeScreen()
            }
            composable(NavDestination.Notes.route) {
                NotesListScreen(
                    viewModel = notesViewModel,
                    onNavigateToCreate = { navController.navigate(NavDestination.CreateNote.route) }
                )
            }
            composable(NavDestination.CreateNote.route) {
                CreateNoteScreen(
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = notesViewModel
                )
            }
            composable(NavDestination.Pomodoro.route) {
                PomodoroScreen()
            }
            composable(NavDestination.KanbanBoard.route) {
                KanbanBoardScreen()
            }
            composable(NavDestination.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSignOut = onSignOutClick
                )
            }
        }
    }
} 