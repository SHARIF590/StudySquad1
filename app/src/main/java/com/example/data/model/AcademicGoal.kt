package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "academic_goals")
data class AcademicGoal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val targetCount: Int = 1,
    val completedCount: Int = 0,
    val isCompleted: Boolean = false,
    val category: String = "Daily Target", // Daily Target, Weekly Target, Exam Chapter
    val dueDateText: String = "Today",
    val createdAt: Long = System.currentTimeMillis()
)
