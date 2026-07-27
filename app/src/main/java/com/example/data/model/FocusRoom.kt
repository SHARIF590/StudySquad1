package com.example.data.model

data class FocusRoom(
    val id: String,
    val roomName: String,
    val subject: String,
    val hostName: String,
    val hostAvatarHex: Long = 0xFF06B6D4,
    val participantNames: List<String>,
    val elapsedSeconds: Long = 0L,
    val targetMinutes: Int = 45,
    val isPrivate: Boolean = false,
    val isActive: Boolean = true,
    val ambientAudioName: String = "Rain Ambient"
)
