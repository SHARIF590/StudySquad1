package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class NudgeChipOption(
    val label: String,
    val nudgeType: String,
    val iconEmoji: String,
    val color: Color
)

@Composable
fun QuickNudgeBar(
    onNudgeSelected: (String, String) -> Unit,
    targetName: String = "Squad",
    modifier: Modifier = Modifier
) {
    val nudgeOptions = listOf(
        NudgeChipOption("Get to Work!", "GET_TO_WORK", "⚡", AmberWarning),
        NudgeChipOption("Keep Going!", "KEEP_GOING", "🔥", CyanAccent),
        NudgeChipOption("Great Session!", "GREAT_JOB", "🎉", MintSuccess),
        NudgeChipOption("Join My Room!", "JOIN_ROOM", "📚", IndigoLight)
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ElectricBolt,
                contentDescription = "Quick Nudge",
                tint = AmberWarning,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Quick Nudge to $targetName",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondaryDark
            )
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(nudgeOptions) { chip ->
                Button(
                    onClick = { onNudgeSelected(targetName, chip.nudgeType) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = chip.color.copy(alpha = 0.2f),
                        contentColor = chip.color
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "${chip.iconEmoji} ${chip.label}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
