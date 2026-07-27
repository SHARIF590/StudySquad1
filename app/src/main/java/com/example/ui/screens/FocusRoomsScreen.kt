package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FocusRoom
import com.example.ui.components.ActiveFocusRoomOverlay
import com.example.ui.components.FocusRoomCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusRoomsScreen(
    focusRooms: List<FocusRoom>,
    activeRoom: FocusRoom?,
    onCreateRoom: (String, String, Int, String) -> Unit,
    onJoinRoom: (String) -> Unit,
    onLeaveRoom: () -> Unit,
    onSendRoomNudge: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var newRoomName by remember { mutableStateOf("") }
    var newSubject by remember { mutableStateOf("Higher Math") }
    var targetMins by remember { mutableStateOf(45) }
    var ambientSound by remember { mutableStateOf("Deep Rain") }

    // If currently inside an active room, render full minimalist focus screen overlay!
    if (activeRoom != null) {
        ActiveFocusRoomOverlay(
            room = activeRoom,
            onLeaveRoom = onLeaveRoom,
            onSendRoomNudge = onSendRoomNudge,
            modifier = modifier
        )
    } else {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = NavyDarkBackground,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { showCreateDialog = true },
                    containerColor = CyanAccent,
                    contentColor = NavyDarkBackground,
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Create Room")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Host Study Room", fontWeight = FontWeight.Bold)
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = "Focus Rooms",
                                tint = CyanAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Focus Rooms (Co-Studying)",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimaryDark,
                                    fontSize = 22.sp
                                )
                            )
                        }
                        Text(
                            text = "Join a live room or host your own session. Study silently alongside friends for accountability.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondaryDark,
                                fontSize = 13.sp
                            )
                        )
                    }
                }

                if (focusRooms.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(32.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = "Empty",
                                    tint = CyanAccent,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No active focus rooms right now",
                                    style = MaterialTheme.typography.titleMedium.copy(color = TextPrimaryDark, fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Host a room and invite your study squad!",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                                )
                            }
                        }
                    }
                } else {
                    items(focusRooms) { room ->
                        FocusRoomCard(
                            room = room,
                            onJoinRoom = onJoinRoom
                        )
                    }
                }
            }

            // Create Focus Room Dialog
            if (showCreateDialog) {
                AlertDialog(
                    onDismissRequest = { showCreateDialog = false },
                    title = {
                        Text(
                            text = "Host a Co-Study Room",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = newRoomName,
                                onValueChange = { newRoomName = it },
                                label = { Text("Room Title (e.g. SSC 2027 Math Sprint)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = newSubject,
                                onValueChange = { newSubject = it },
                                label = { Text("Subject (e.g. Physics, Higher Math)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Target Duration:", fontSize = 13.sp, color = TextPrimaryDark)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf(30, 45, 60).forEach { mins ->
                                        FilterChip(
                                            selected = targetMins == mins,
                                            onClick = { targetMins = mins },
                                            label = { Text("${mins}m") }
                                        )
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val name = if (newRoomName.isBlank()) "SSC 2027 Study Sprint 🚀" else newRoomName
                                onCreateRoom(name, newSubject, targetMins, ambientSound)
                                showCreateDialog = false
                                newRoomName = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDarkBackground)
                        ) {
                            Text("Start Room", fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showCreateDialog = false }) {
                            Text("Cancel", color = TextSecondaryDark)
                        }
                    },
                    containerColor = NavyDarkCard
                )
            }
        }
    }
}
