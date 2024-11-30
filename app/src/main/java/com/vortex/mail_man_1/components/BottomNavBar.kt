package com.vortex.mail_man_1.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.vortex.mail_man_1.R
import com.vortex.mail_man_1.navigation.NavDestination

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (NavDestination) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == NavDestination.Home.route,
            onClick = { onNavigate(NavDestination.Home) },
            icon = { Icon(painter = painterResource(R.drawable.ic_home), contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == NavDestination.Pomodoro.route,
            onClick = { onNavigate(NavDestination.Pomodoro) },
            icon = { Icon(painter = painterResource(R.drawable.ic_timer), contentDescription = "Pomodoro") },
            label = { Text("Pomodoro") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onNavigate(NavDestination.CreateNote) },
            icon = { Icon(painter = painterResource(R.drawable.ic_add), contentDescription = "Create") },
            label = { Text("Create") }
        )
        NavigationBarItem(
            selected = currentRoute == NavDestination.KanbanBoard.route,
            onClick = { onNavigate(NavDestination.KanbanBoard) },
            icon = { Icon(painter = painterResource(R.drawable.ic_kanban), contentDescription = "Kanban") },
            label = { Text("Kanban") }
        )
        NavigationBarItem(
            selected = currentRoute == NavDestination.Settings.route,
            onClick = { onNavigate(NavDestination.Settings) },
            icon = { Icon(painter = painterResource(R.drawable.ic_settings), contentDescription = "Settings") },
            label = { Text("Settings") }
        )
    }
} 