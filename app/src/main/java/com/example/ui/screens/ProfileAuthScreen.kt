package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAuthScreen(
    userProfile: UserProfile?,
    onUpdateProfile: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var displayName by remember(userProfile) { mutableStateOf(userProfile?.displayName ?: "Sharif") }
    var studyFocus by remember(userProfile) { mutableStateOf(userProfile?.studyFocus ?: "SSC 2027 Higher Math & Physics") }
    var emailInput by remember { mutableStateOf("jacksonmd456@gmail.com") }
    var isGoogleSignedIn by remember { mutableStateOf(true) }
    var showSaveToast by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = CyanAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "User Profile & Auth",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimaryDark,
                            fontSize = 22.sp
                        )
                    )
                }
                Text(
                    text = "Manage your StudySquad profile, academic focus, and account authentication.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark, fontSize = 13.sp)
                )
            }
        }

        // Authentication Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(IndigoPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Account",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isGoogleSignedIn) "Signed in with Google" else "Email Auth Mode",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = emailInput,
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MintSuccess.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "VERIFIED",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintSuccess
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { isGoogleSignedIn = !isGoogleSignedIn },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent)
                        ) {
                            Icon(
                                imageVector = if (isGoogleSignedIn) Icons.Default.Lock else Icons.Default.VpnKey,
                                contentDescription = "Toggle",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isGoogleSignedIn) "Switch to Email" else "Google Sign-In", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Profile Details Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Academic Profile Settings",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )

                    OutlinedTextField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        label = { Text("Display Name (e.g. Sharif)") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyanAccent) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NavyDarkBackground,
                            unfocusedContainerColor = NavyDarkBackground,
                            focusedBorderColor = CyanAccent,
                            unfocusedBorderColor = NavyDarkBorder,
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        )
                    )

                    OutlinedTextField(
                        value = studyFocus,
                        onValueChange = { studyFocus = it },
                        label = { Text("Current Study Focus") },
                        leadingIcon = { Icon(Icons.Default.School, contentDescription = null, tint = CyanAccent) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NavyDarkBackground,
                            unfocusedContainerColor = NavyDarkBackground,
                            focusedBorderColor = CyanAccent,
                            unfocusedBorderColor = NavyDarkBorder,
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        )
                    )

                    Button(
                        onClick = {
                            if (displayName.isNotBlank()) {
                                onUpdateProfile(displayName, studyFocus)
                                showSaveToast = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDarkBackground)
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Profile Settings", fontWeight = FontWeight.Bold)
                    }

                    if (showSaveToast) {
                        Text(
                            text = "✓ Profile updated successfully!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintSuccess,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        // Stats Summary
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Accountability Statistics",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Focused Study Time", fontSize = 11.sp, color = TextSecondaryDark)
                            Text("${userProfile?.totalStudyMinutes ?: 240} Mins", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Current Streak", fontSize = 11.sp, color = TextSecondaryDark)
                            Text("${userProfile?.streakDays ?: 12} Days 🔥", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AmberWarning)
                        }
                    }
                }
            }
        }
    }
}
