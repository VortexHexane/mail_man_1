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
        val items = listOf(
            NavDestination.Home,
            NavDestination.Pomodoro,
            NavDestination.CreateNote,
            NavDestination.KanbanBoard,
            NavDestination.Settings
        )
        
        items.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute.startsWith(destination.route),
                onClick = { onNavigate(destination) },
                icon = {
                    when (destination) {
                        NavDestination.Home -> Icon(painter = painterResource(R.drawable.ic_home), "Home")
                        NavDestination.Pomodoro -> Icon(painter = painterResource(R.drawable.ic_timer), "Pomodoro")
                        NavDestination.CreateNote -> Icon(painter = painterResource(R.drawable.ic_add), "Create")
                        NavDestination.KanbanBoard -> Icon(painter = painterResource(R.drawable.ic_kanban), "Kanban")
                        NavDestination.Settings -> Icon(painter = painterResource(R.drawable.ic_settings), "Settings")
                        NavDestination.Auth -> TODO()
                        NavDestination.Notes -> TODO()
                    }
                },
                label = { 
                    Text(when (destination) {
                        NavDestination.CreateNote -> "Create"
                        else -> destination.route.replaceFirstChar { it.uppercase() }
                    })
                },
                alwaysShowLabel = true
            )
        }
    }
} 