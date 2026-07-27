package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FocusRoom
import com.example.ui.theme.*

@Composable
fun ActiveFocusRoomOverlay(
    room: FocusRoom,
    onLeaveRoom: () -> Unit,
    onSendRoomNudge: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPaused by remember { mutableStateOf(false) }
    var selectedAmbient by remember { mutableStateOf(room.ambientAudioName) }
    val ambientOptions = listOf("Deep Rain", "Lofi Beats", "Cafe Ambient", "Alpha Waves", "Mute")

    val hours = room.elapsedSeconds / 3600
    val minutes = (room.elapsedSeconds % 3600) / 60
    val seconds = room.elapsedSeconds % 60
    val formattedTime = if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }

    // Breathing pulse effect for focus circle
    val infiniteTransition = rememberInfiniteTransition(label = "timerPulse")
    val borderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderAlpha"
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = NavyDarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MintSuccess.copy(alpha = 0.2f),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(MintSuccess, CyanAccent)))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(MintSuccess)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LIVE CO-STUDYING",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintSuccess
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = room.roomName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark,
                            fontSize = 20.sp
                        )
                    )
                }

                IconButton(
                    onClick = onLeaveRoom,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(NavyDarkCard)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Room",
                        tint = TextPrimaryDark
                    )
                }
            }

            // Central Minimalist Timer Ring
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(260.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    IndigoPrimary.copy(alpha = 0.25f),
                                    NavyDarkCard
                                )
                            )
                        )
                        .border(
                            width = 4.dp,
                            brush = Brush.sweepGradient(
                                listOf(CyanAccent.copy(alpha = borderAlpha), IndigoLight, MintSuccess, CyanAccent)
                            ),
                            shape = CircleShape
                        )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = room.subject.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CyanAccent,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = formattedTime,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 48.sp,
                                color = TextPrimaryDark
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isPaused) "PAUSED" else "FOCUS SPRINT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPaused) AmberWarning else MintSuccess
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Ambient Sound Controller
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = "Ambient",
                                tint = CyanAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Focus Ambient Audio",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(ambientOptions) { sound ->
                                val isSelected = sound == selectedAmbient
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedAmbient = sound },
                                    label = { Text(sound, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = CyanAccent,
                                        selectedLabelColor = NavyDarkBackground,
                                        containerColor = NavyDarkBackground,
                                        labelColor = TextSecondaryDark
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Co-studiers in room
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Studying Together Right Now",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondaryDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(room.participantNames) { participant ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = NavyDarkCard,
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(IndigoLight.copy(alpha = 0.5f), CyanAccent.copy(alpha = 0.5f))))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(MintSuccess)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = participant,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                            }
                        }
                    }
                }
            }

            // Room Bottom Actions (Pause, Room Nudge, Exit)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { isPaused = !isPaused },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberWarning),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(AmberWarning, AmberWarning)))
                ) {
                    Icon(
                        imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = "Pause",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isPaused) "Resume" else "Pause", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onSendRoomNudge("🔥 Squad focus power! Keep going!") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDarkBackground)
                ) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = "Nudge",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Nudge Room", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onLeaveRoom,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseDanger, contentColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Finish",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Finish", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
