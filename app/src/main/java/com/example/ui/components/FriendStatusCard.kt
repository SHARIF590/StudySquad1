package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FriendItem
import com.example.ui.theme.*

@Composable
fun FriendStatusCard(
    friend: FriendItem,
    onSendNudge: () -> Unit,
    onJoinRoom: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isStudying = friend.status.equals("Studying", ignoreCase = true) || friend.status.equals("In Focus Room", ignoreCase = true)
    
    // Live pulse animation for active study state
    val infiniteTransition = rememberInfiniteTransition(label = "dotPulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (isStudying) MintSuccess.copy(alpha = 0.5f) else NavyDarkBorder,
                RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(friend.avatarColorHex)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = friend.name.take(1).uppercase(),
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }

                        // Status dot badge
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        friend.status == "Studying" -> MintSuccess.copy(alpha = dotAlpha)
                                        friend.status == "In Focus Room" -> CyanAccent.copy(alpha = dotAlpha)
                                        else -> AmberWarning
                                    }
                                )
                                .border(2.dp, NavyDarkCard, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = friend.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark,
                                fontSize = 15.sp
                            )
                        )
                        Text(
                            text = friend.studyFocus,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Status chip tag
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (friend.status) {
                        "Studying" -> MintSuccess.copy(alpha = 0.15f)
                        "In Focus Room" -> CyanAccent.copy(alpha = 0.15f)
                        else -> NavyDarkBackground
                    },
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(
                            when (friend.status) {
                                "Studying" -> MintSuccess
                                "In Focus Room" -> CyanAccent
                                else -> TextSecondaryDark
                            }
                        )
                    )
                ) {
                    Text(
                        text = friend.status.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (friend.status) {
                            "Studying" -> MintSuccess
                            "In Focus Room" -> CyanAccent
                            else -> TextSecondaryDark
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Current subject & daily progress
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavyDarkBackground)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Current Working Subject",
                        fontSize = 10.sp,
                        color = TextSecondaryDark
                    )
                    Text(
                        text = friend.currentSubject,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyanAccentLight
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Today's Study",
                        fontSize = 10.sp,
                        color = TextSecondaryDark
                    )
                    Text(
                        text = "${friend.studyMinutesToday} mins",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MintSuccess
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions: Quick Nudge & Join Room buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onSendNudge,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberWarning),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(AmberWarning.copy(alpha = 0.6f)))
                ) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = "Nudge",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Nudge", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                if (friend.activeRoomId != null && onJoinRoom != null) {
                    Button(
                        onClick = { onJoinRoom(friend.activeRoomId) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDarkBackground)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MeetingRoom,
                            contentDescription = "Join Room",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Join Room", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
