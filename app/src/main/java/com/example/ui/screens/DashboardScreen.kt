package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.data.model.FriendItem
import com.example.data.model.UserProfile
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.ExamCountdownState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userProfile: UserProfile?,
    friends: List<FriendItem>,
    focusRooms: List<FocusRoom>,
    countdownState: ExamCountdownState,
    onStartStudying: (String) -> Unit,
    onStopStudying: () -> Unit,
    onSendNudge: (String, String) -> Unit,
    onJoinRoom: (String) -> Unit,
    onNavigateToRooms: () -> Unit,
    onNavigateToChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubject by remember { mutableStateOf("Physics - Dynamics") }
    var isSubjectDropdownOpen by remember { mutableStateOf(false) }
    val subjectsList = listOf("Physics - Dynamics", "Higher Math - Integration", "Chemistry - Organic", "Biology - Chapter 4", "General Math - Algebra")

    val isUserStudying = userProfile?.currentStatus == "Studying" || userProfile?.currentStatus == "In Focus Room"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Welcome Header & Profile Summary
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, ${userProfile?.displayName ?: "Sharif"} 👋",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimaryDark,
                            fontSize = 24.sp
                        )
                    )
                    Text(
                        text = userProfile?.studyFocus ?: "SSC 2027 Higher Math & Physics",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CyanAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NavyDarkCard,
                    onClick = onNavigateToChat
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubble,
                            contentDescription = "Chat",
                            tint = CyanAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Squad Chat",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    }
                }
            }
        }

        // Permanent Prominent SSC 2027 Countdown Widget
        item {
            SSC2027CountdownWidget(
                countdownState = countdownState,
                examName = userProfile?.targetExamName ?: "SSC 2027 Examination"
            )
        }

        // User Live Status Switcher Bar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(if (isUserStudying) MintSuccess else AmberWarning)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isUserStudying) "NOW STUDYING" else "SET YOUR LIVE STATUS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isUserStudying) MintSuccess else AmberWarning
                            )
                        }

                        if (isUserStudying) {
                            TextButton(
                                onClick = onStopStudying,
                                colors = ButtonDefaults.textButtonColors(contentColor = RoseDanger)
                            ) {
                                Text("End Session", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!isUserStudying) {
                        ExposedDropdownMenuBox(
                            expanded = isSubjectDropdownOpen,
                            onExpandedChange = { isSubjectDropdownOpen = !isSubjectDropdownOpen }
                        ) {
                            OutlinedTextField(
                                value = selectedSubject,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Active Subject Focus") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSubjectDropdownOpen) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = NavyDarkBackground,
                                    unfocusedContainerColor = NavyDarkBackground,
                                    focusedBorderColor = CyanAccent,
                                    unfocusedBorderColor = NavyDarkBorder,
                                    focusedLabelColor = CyanAccent,
                                    unfocusedLabelColor = TextSecondaryDark,
                                    focusedTextColor = TextPrimaryDark,
                                    unfocusedTextColor = TextPrimaryDark
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = isSubjectDropdownOpen,
                                onDismissRequest = { isSubjectDropdownOpen = false }
                            ) {
                                subjectsList.forEach { subj ->
                                    DropdownMenuItem(
                                        text = { Text(subj, color = TextPrimaryDark) },
                                        onClick = {
                                            selectedSubject = subj
                                            isSubjectDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onStartStudying(selectedSubject) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary, contentColor = Color.White)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start Solo Study Session", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(NavyDarkBackground)
                                .padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = "Subject",
                                tint = CyanAccent
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = userProfile?.currentSubject ?: selectedSubject,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryDark
                                    )
                                )
                                Text(
                                    text = "Connected friends see you live on the status board!",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark, fontSize = 11.sp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Focus Rooms Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "Rooms",
                        tint = CyanAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Active Co-Study Rooms",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark,
                            fontSize = 18.sp
                        )
                    )
                }

                TextButton(
                    onClick = onNavigateToRooms,
                    colors = ButtonDefaults.textButtonColors(contentColor = CyanAccent)
                ) {
                    Text("See All Rooms →", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Active Focus Rooms items preview
        if (focusRooms.isNotEmpty()) {
            item {
                FocusRoomCard(
                    room = focusRooms.first(),
                    onJoinRoom = onJoinRoom
                )
            }
        }

        // Quick Squad Nudges
        item {
            QuickNudgeBar(
                onNudgeSelected = { friendName, nudgeType ->
                    onSendNudge(friendName, nudgeType)
                }
            )
        }

        // Live Friends Status Board Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(MintSuccess)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Live Squad Status Board",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark,
                            fontSize = 18.sp
                        )
                    )
                }

                Text(
                    text = "${friends.count { it.isOnline }} Online",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintSuccess
                )
            }
        }

        // Friends List
        items(friends) { friend ->
            FriendStatusCard(
                friend = friend,
                onSendNudge = { onSendNudge(friend.name, "KEEP_GOING") },
                onJoinRoom = onJoinRoom
            )
        }
    }
}
