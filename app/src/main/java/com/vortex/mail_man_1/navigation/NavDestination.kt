package com.vortex.mail_man_1.navigation

sealed class NavDestination(val route: String) {
    object Home : NavDestination("home")
    object Pomodoro : NavDestination("pomodoro")
    object CreateNote : NavDestination("create_note")
    object KanbanBoard : NavDestination("kanban")
    object Settings : NavDestination("settings")
} 