package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val displayName: String = "Sharif",
    val studyFocus: String = "SSC 2027 Higher Math & Physics",
    val targetExamName: String = "SSC 2027 Examination",
    val targetExamTimestamp: Long = 1798761600000L, // Feb 1, 2027
    val currentStatus: String = "Idle", // Studying, Break, Idle
    val currentSubject: String = "Physics - Dynamics",
    val totalStudyMinutes: Long = 240L,
    val streakDays: Int = 12
)
