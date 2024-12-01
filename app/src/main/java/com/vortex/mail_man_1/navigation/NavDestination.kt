package com.vortex.mail_man_1.navigation

sealed class NavDestination(val route: String) {
    object Home : NavDestination("home")
    object Auth : NavDestination("auth")
    object Notes : NavDestination("notes")
    object Pomodoro : NavDestination("pomodoro")
    object CreateNote : NavDestination("create_note")
    object KanbanBoard : NavDestination("kanban")
    object Settings : NavDestination("settings")
} 