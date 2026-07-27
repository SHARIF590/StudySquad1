package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.ExamCountdownState

@Composable
fun SSC2027CountdownWidget(
    countdownState: ExamCountdownState,
    modifier: Modifier = Modifier,
    examName: String = "SSC 2027 Examination"
) {
    // Pulse animation for countdown urgency badge
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(pulseScale)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(IndigoLight.copy(alpha = 0.6f), CyanAccent.copy(alpha = 0.6f))),
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = NavyDarkCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            IndigoPrimary.copy(alpha = 0.25f),
                            NavyDarkCard
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(IndigoPrimary.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Exam",
                            tint = CyanAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = examName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = "Target Date: February 2027",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondaryDark,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = AmberWarning.copy(alpha = 0.2f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AmberWarning, CyanAccent)))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = "Urgent",
                            tint = AmberWarning,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "OFFICIAL COUNTDOWN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AmberWarning
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Timer Unit Boxes (Days, Hours, Minutes, Seconds)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeUnitBox(value = countdownState.days.toString().padStart(3, '0'), label = "DAYS", highlightColor = CyanAccent)
                Text(":", color = TextSecondaryDark, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                TimeUnitBox(value = countdownState.hours.toString().padStart(2, '0'), label = "HOURS", highlightColor = IndigoLight)
                Text(":", color = TextSecondaryDark, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                TimeUnitBox(value = countdownState.minutes.toString().padStart(2, '0'), label = "MINS", highlightColor = MintSuccess)
                Text(":", color = TextSecondaryDark, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                TimeUnitBox(value = countdownState.seconds.toString().padStart(2, '0'), label = "SECS", highlightColor = AmberWarning)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar toward target
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Preparation Mileage",
                        style = MaterialTheme.typography.labelMedium.copy(color = TextSecondaryDark)
                    )
                    Text(
                        text = "${(countdownState.totalProgressPercent * 100).toInt()}% Ready",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = CyanAccent,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { countdownState.totalProgressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = CyanAccent,
                    trackColor = NavyDarkBorder
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Motivational squad quote
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavyDarkBackground.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Quote",
                    tint = AmberWarning,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "“Daily 3 hours study = Guaranteed SSC GPA 5.00 success!”",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextPrimaryDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
private fun TimeUnitBox(
    value: String,
    label: String,
    highlightColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NavyDarkBackground)
            .border(0.8.dp, highlightColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                color = highlightColor,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondaryDark,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
