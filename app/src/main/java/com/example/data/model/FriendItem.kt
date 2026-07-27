package com.example.data.model

data class FriendItem(
    val id: String,
    val name: String,
    val studyFocus: String,
    val isOnline: Boolean,
    val status: String, // "Studying", "Idle", "In Focus Room", "On Break"
    val currentSubject: String,
    val studyMinutesToday: Int,
    val activeRoomId: String? = null,
    val avatarColorHex: Long = 0xFF4F46E5,
    val lastActiveTime: String = "Just now"
)
