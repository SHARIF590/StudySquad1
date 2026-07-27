package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderId: String,
    val senderName: String,
    val text: String,
    val isNudge: Boolean = false,
    val nudgeType: String? = null, // "GET_TO_WORK", "KEEP_GOING", "GREAT_JOB", "JOIN_ROOM"
    val timestamp: Long = System.currentTimeMillis(),
    val isFromUser: Boolean = false
)
