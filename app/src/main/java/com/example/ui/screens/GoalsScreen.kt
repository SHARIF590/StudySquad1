package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AcademicGoal
import com.example.data.model.UserProfile
import com.example.ui.components.SSC2027CountdownWidget
import com.example.ui.theme.*
import com.example.viewmodel.ExamCountdownState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    goals: List<AcademicGoal>,
    userProfile: UserProfile?,
    countdownState: ExamCountdownState,
    onAddGoal: (String, String, Int, String) -> Unit,
    onToggleGoal: (Long, Boolean) -> Unit,
    onDeleteGoal: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var goalTitle by remember { mutableStateOf("") }
    var goalSubject by remember { mutableStateOf("Higher Math") }
    var goalCategory by remember { mutableStateOf("Daily Target") }

    val completedCount = goals.count { it.isCompleted }
    val totalCount = goals.size
    val completionPercent = if (totalCount > 0) (completedCount.toFloat() / totalCount) else 0f

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = NavyDarkBackground,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = IndigoPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Goal")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Target Goal", fontWeight = FontWeight.Bold)
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
                            imageVector = Icons.Default.TrackChanges,
                            contentDescription = "Goals",
                            tint = CyanAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Goals & Milestone Tracker",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimaryDark,
                                fontSize = 22.sp
                            )
                        )
                    }
                    Text(
                        text = "Set daily targets and keep your eye on the SSC 2027 milestone.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark, fontSize = 13.sp)
                    )
                }
            }

            // Permanent Prominent Countdown Widget
            item {
                SSC2027CountdownWidget(
                    countdownState = countdownState,
                    examName = userProfile?.targetExamName ?: "SSC 2027 Examination"
                )
            }

            // Milestone Summary Card
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
                            Text(
                                text = "Daily Goal Progress",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                            )
                            Text(
                                text = "$completedCount / $totalCount Completed",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintSuccess
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { completionPercent },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MintSuccess,
                            trackColor = NavyDarkBorder
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${userProfile?.totalStudyMinutes ?: 240} Mins",
                                    fontWeight = FontWeight.Black,
                                    color = CyanAccent,
                                    fontSize = 18.sp
                                )
                                Text("Total Study", fontSize = 11.sp, color = TextSecondaryDark)
                            }

                            Divider(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(1.dp),
                                color = NavyDarkBorder
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${userProfile?.streakDays ?: 12} Days",
                                    fontWeight = FontWeight.Black,
                                    color = AmberWarning,
                                    fontSize = 18.sp
                                )
                                Text("Study Streak", fontSize = 11.sp, color = TextSecondaryDark)
                            }
                        }
                    }
                }
            }

            // Goals Checklist Section Title
            item {
                Text(
                    text = "Academic Targets",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        fontSize = 18.sp
                    )
                )
            }

            // Goals List
            items(goals, key = { it.id }) { goal ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (goal.isCompleted) NavyDarkCard.copy(alpha = 0.5f) else NavyDarkCard
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = goal.isCompleted,
                                onCheckedChange = { checked ->
                                    onToggleGoal(goal.id, checked)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MintSuccess,
                                    uncheckedColor = TextSecondaryDark
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Text(
                                    text = goal.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (goal.isCompleted) TextSecondaryDark else TextPrimaryDark,
                                        textDecoration = if (goal.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                        fontSize = 14.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = IndigoPrimary.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = goal.subject,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            fontSize = 10.sp,
                                            color = IndigoLight,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = NavyDarkBackground
                                    ) {
                                        Text(
                                            text = goal.category,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            fontSize = 10.sp,
                                            color = TextSecondaryDark
                                        )
                                    }
                                }
                            }
                        }

                        IconButton(onClick = { onDeleteGoal(goal.id) }) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete",
                                tint = TextSecondaryDark.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Add Goal Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add Academic Target", fontWeight = FontWeight.Bold, color = TextPrimaryDark) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = goalTitle,
                            onValueChange = { goalTitle = it },
                            label = { Text("Target Title (e.g. Solve 50 MCQ Math)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = goalSubject,
                            onValueChange = { goalSubject = it },
                            label = { Text("Subject Focus") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (goalTitle.isNotBlank()) {
                                onAddGoal(goalTitle, goalSubject, 1, goalCategory)
                                showAddDialog = false
                                goalTitle = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Text("Add Target", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel", color = TextSecondaryDark)
                    }
                },
                containerColor = NavyDarkCard
            )
        }
    }
}
