package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.StudySquadViewModel

@Composable
fun StudySquadNavGraph(
    navController: NavHostController,
    viewModel: StudySquadViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val friends by viewModel.friends.collectAsState()
    val focusRooms by viewModel.focusRooms.collectAsState()
    val activeRoom by viewModel.activeUserRoom.collectAsState()
    val goals by viewModel.goals.collectAsState()
    val messages by viewModel.chatMessages.collectAsState()
    val countdownState by viewModel.countdownState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screens = listOf(
        Screen.Dashboard,
        Screen.FocusRooms,
        Screen.Goals,
        Screen.Chat,
        Screen.Profile
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = NavyDarkBackground,
        bottomBar = {
            if (activeRoom == null) {
                NavigationBar(
                    containerColor = NavyDarkCard,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    screens.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title,
                                    tint = if (selected) CyanAccent else TextSecondaryDark
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selected) CyanAccent else TextSecondaryDark
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = IndigoPrimary.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    userProfile = userProfile,
                    friends = friends,
                    focusRooms = focusRooms,
                    countdownState = countdownState,
                    onStartStudying = { subject -> viewModel.startStudying(subject) },
                    onStopStudying = { viewModel.stopStudying() },
                    onSendNudge = { friendName, nudgeType -> viewModel.sendNudge(friendName, nudgeType) },
                    onJoinRoom = { roomId ->
                        viewModel.joinFocusRoom(roomId)
                        navController.navigate(Screen.FocusRooms.route)
                    },
                    onNavigateToRooms = { navController.navigate(Screen.FocusRooms.route) },
                    onNavigateToChat = { navController.navigate(Screen.Chat.route) }
                )
            }

            composable(Screen.FocusRooms.route) {
                FocusRoomsScreen(
                    focusRooms = focusRooms,
                    activeRoom = activeRoom,
                    onCreateRoom = { name, subj, mins, audio ->
                        viewModel.createFocusRoom(name, subj, mins, audio)
                    },
                    onJoinRoom = { roomId -> viewModel.joinFocusRoom(roomId) },
                    onLeaveRoom = { viewModel.leaveFocusRoom() },
                    onSendRoomNudge = { text -> viewModel.sendChatMessage(text) }
                )
            }

            composable(Screen.Goals.route) {
                GoalsScreen(
                    goals = goals,
                    userProfile = userProfile,
                    countdownState = countdownState,
                    onAddGoal = { title, subj, target, cat ->
                        viewModel.addGoal(title, subj, target, cat)
                    },
                    onToggleGoal = { id, comp -> viewModel.toggleGoal(id, comp) },
                    onDeleteGoal = { id -> viewModel.deleteGoal(id) }
                )
            }

            composable(Screen.Chat.route) {
                ChatScreen(
                    messages = messages,
                    onSendMessage = { text -> viewModel.sendChatMessage(text) },
                    onSendNudge = { friendName, nudgeType -> viewModel.sendNudge(friendName, nudgeType) }
                )
            }

            composable(Screen.Profile.route) {
                ProfileAuthScreen(
                    userProfile = userProfile,
                    onUpdateProfile = { name, focus ->
                        viewModel.updateUserProfile(name, focus)
                    }
                )
            }
        }
    }
}
