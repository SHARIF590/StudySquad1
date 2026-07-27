package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FocusRoom
import com.example.ui.theme.*

@Composable
fun FocusRoomCard(
    room: FocusRoom,
    onJoinRoom: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = room.elapsedSeconds / 60
    val seconds = room.elapsedSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, CyanAccent.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = NavyDarkCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MintSuccess)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = room.subject.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CyanAccent
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = AmberWarning,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = timeFormatted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = room.roomName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = "Audio",
                    tint = TextSecondaryDark,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Ambient: ${room.ambientAudioName}",
                    fontSize = 11.sp,
                    color = TextSecondaryDark
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Participant avatars & Join button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "Members",
                        tint = TextSecondaryDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${room.participantNames.size} Co-Studying",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimaryDark
                    )
                }

                Button(
                    onClick = { onJoinRoom(room.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDarkBackground)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Join",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Join Room", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
