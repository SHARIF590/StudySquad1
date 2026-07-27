package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Status Board", Icons.Default.Dashboard)
    object FocusRooms : Screen("focus_rooms", "Focus Rooms", Icons.Default.Group)
    object Goals : Screen("goals", "Goals", Icons.Default.TrackChanges)
    object Chat : Screen("chat", "Chat", Icons.Default.ChatBubble)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}
