package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AcademicGoal
import com.example.data.model.ChatMessage
import com.example.data.model.FocusRoom
import com.example.data.model.FriendItem
import com.example.data.model.UserProfile
import com.example.data.repository.StudySquadRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class ExamCountdownState(
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0,
    val isExpired: Boolean = false,
    val totalProgressPercent: Float = 0.65f
)

class StudySquadViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val repository = StudySquadRepository(
        userDao = database.userDao(),
        goalDao = database.goalDao(),
        chatDao = database.chatDao(),
        externalScope = viewModelScope
    )

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val friends: StateFlow<List<FriendItem>> = repository.friendsState
    val focusRooms: StateFlow<List<FocusRoom>> = repository.focusRoomsState
    val activeUserRoom: StateFlow<FocusRoom?> = repository.activeUserRoom

    val goals: StateFlow<List<AcademicGoal>> = repository.goals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _countdownState = MutableStateFlow(ExamCountdownState())
    val countdownState: StateFlow<ExamCountdownState> = _countdownState.asStateFlow()

    // SSC 2027 Exam Date: Default Feb 1, 2027 09:00:00 AM UTC
    val ssc2027TargetTimestamp = 1798794000000L

    init {
        viewModelScope.launch {
            while (true) {
                calculateCountdown()
                delay(1000L)
            }
        }
    }

    private fun calculateCountdown() {
        val now = System.currentTimeMillis()
        val diff = ssc2027TargetTimestamp - now
        if (diff <= 0) {
            _countdownState.value = ExamCountdownState(0, 0, 0, 0, isExpired = true, totalProgressPercent = 1.0f)
        } else {
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60
            
            // Assuming 365 days prep window
            val elapsedDays = 365 - days.coerceAtMost(365)
            val progress = (elapsedDays.toFloat() / 365f).coerceIn(0.1f, 0.99f)

            _countdownState.value = ExamCountdownState(
                days = days,
                hours = hours,
                minutes = minutes,
                seconds = seconds,
                isExpired = false,
                totalProgressPercent = progress
            )
        }
    }

    fun updateUserProfile(name: String, focus: String) {
        viewModelScope.launch {
            repository.updateUserProfile(
                name = name,
                focus = focus,
                examName = "SSC 2027 Examination",
                targetTimestamp = ssc2027TargetTimestamp
            )
        }
    }

    fun startStudying(subject: String) {
        viewModelScope.launch {
            repository.setUserStatus("Studying", subject)
        }
    }

    fun stopStudying() {
        viewModelScope.launch {
            repository.setUserStatus("Idle", "None")
        }
    }

    fun addGoal(title: String, subject: String, targetCount: Int, category: String) {
        viewModelScope.launch {
            repository.addGoal(title, subject, targetCount, category)
        }
    }

    fun toggleGoal(goalId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleGoal(goalId, isCompleted)
        }
    }

    fun deleteGoal(goalId: Long) {
        viewModelScope.launch {
            repository.deleteGoal(goalId)
        }
    }

    fun createFocusRoom(roomName: String, subject: String, targetMinutes: Int, ambientAudio: String) {
        viewModelScope.launch {
            repository.createFocusRoom(roomName, subject, targetMinutes, ambientAudio)
            repository.setUserStatus("In Focus Room", subject)
        }
    }

    fun joinFocusRoom(roomId: String) {
        viewModelScope.launch {
            repository.joinFocusRoom(roomId)
            val room = focusRooms.value.find { it.id == roomId }
            if (room != null) {
                repository.setUserStatus("In Focus Room", room.subject)
            }
        }
    }

    fun leaveFocusRoom() {
        viewModelScope.launch {
            val room = activeUserRoom.value
            if (room != null) {
                val studiedMins = (room.elapsedSeconds / 60).coerceAtLeast(1)
                repository.logCompletedSessionMinutes(studiedMins)
            }
            repository.leaveCurrentFocusRoom()
            repository.setUserStatus("Idle", "None")
        }
    }

    fun sendChatMessage(text: String) {
        viewModelScope.launch {
            repository.sendChatMessage(text)
        }
    }

    fun sendNudge(friendName: String, nudgeType: String) {
        viewModelScope.launch {
            repository.sendNudge(friendName, nudgeType)
        }
    }
}
