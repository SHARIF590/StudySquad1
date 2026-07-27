package com.example.data.repository

import com.example.data.local.ChatDao
import com.example.data.local.GoalDao
import com.example.data.local.UserDao
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StudySquadRepository(
    private val userDao: UserDao,
    private val goalDao: GoalDao,
    private val chatDao: ChatDao,
    private val externalScope: CoroutineScope
) {
    val userProfile: Flow<UserProfile?> = userDao.getUserProfile()
    val goals: Flow<List<AcademicGoal>> = goalDao.getAllGoals()
    val chatMessages: Flow<List<ChatMessage>> = chatDao.getAllMessages()

    // Real-time dynamic state flows for connected friends and focus rooms
    private val _friendsState = MutableStateFlow<List<FriendItem>>(initialMockFriends())
    val friendsState: StateFlow<List<FriendItem>> = _friendsState.asStateFlow()

    private val _focusRoomsState = MutableStateFlow<List<FocusRoom>>(initialMockFocusRooms())
    val focusRoomsState: StateFlow<List<FocusRoom>> = _focusRoomsState.asStateFlow()

    private val _activeUserRoom = MutableStateFlow<FocusRoom?>(null)
    val activeUserRoom: StateFlow<FocusRoom?> = _activeUserRoom.asStateFlow()

    init {
        // Pre-populate database with default initial data if empty
        externalScope.launch(Dispatchers.IO) {
            if (userDao.getUserProfileOnce() == null) {
                userDao.saveUserProfile(UserProfile())
            }
            
            // Default sample goals
            val existingGoals = goalDao.getAllGoals().firstOrNull()
            if (existingGoals.isNullOrEmpty()) {
                goalDao.insertGoal(AcademicGoal(title = "Solve 40 Higher Math MCQ Problems", subject = "Higher Math", targetCount = 40, category = "Daily Target"))
                goalDao.insertGoal(AcademicGoal(title = "Physics Chapter 4 - Dynamics Revision", subject = "Physics", targetCount = 1, category = "Daily Target"))
                goalDao.insertGoal(AcademicGoal(title = "Chemistry Organic Reactions Notes", subject = "Chemistry", targetCount = 1, category = "Weekly Target"))
                goalDao.insertGoal(AcademicGoal(title = "SSC 2027 Model Test 01 - Biology", subject = "Biology", targetCount = 1, category = "Exam Chapter"))
            }

            // Default sample chat messages
            val existingChat = chatDao.getAllMessages().firstOrNull()
            if (existingChat.isNullOrEmpty()) {
                chatDao.insertMessage(ChatMessage(senderId = "friend_1", senderName = "Sharif", text = "Hey squad! Target today: 3 hours of Higher Math & Physics revision.", timestamp = System.currentTimeMillis() - 3600000))
                chatDao.insertMessage(ChatMessage(senderId = "friend_2", senderName = "Anik", text = "Count me in! I'm joining the Physics Focus Room now 🔥", timestamp = System.currentTimeMillis() - 1800000))
                chatDao.insertMessage(ChatMessage(senderId = "friend_3", senderName = "Taskin", text = "Sending everyone a study nudge! Let's hit our SSC 2027 targets 💪", isNudge = true, nudgeType = "KEEP_GOING", timestamp = System.currentTimeMillis() - 600000))
            }
        }

        // Live timer tick for active focus rooms and friends studying
        externalScope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000L)
                updateLiveStudyTimers()
            }
        }
    }

    private fun updateLiveStudyTimers() {
        // Increment elapsed seconds for active rooms
        _focusRoomsState.update { rooms ->
            rooms.map { room ->
                if (room.isActive) room.copy(elapsedSeconds = room.elapsedSeconds + 1) else room
            }
        }

        // Increment active user room if present
        _activeUserRoom.update { room ->
            if (room != null && room.isActive) room.copy(elapsedSeconds = room.elapsedSeconds + 1) else room
        }
    }

    // User profile updates
    suspend fun updateUserProfile(name: String, focus: String, examName: String, targetTimestamp: Long) {
        val current = userDao.getUserProfileOnce() ?: UserProfile()
        userDao.saveUserProfile(current.copy(
            displayName = name,
            studyFocus = focus,
            targetExamName = examName,
            targetExamTimestamp = targetTimestamp
        ))
    }

    suspend fun setUserStatus(status: String, subject: String) {
        userDao.updateStatus(status, subject)
    }

    suspend fun logCompletedSessionMinutes(minutes: Long) {
        userDao.addStudyTime(minutes)
    }

    // Goal Management
    suspend fun addGoal(title: String, subject: String, targetCount: Int, category: String) {
        goalDao.insertGoal(AcademicGoal(title = title, subject = subject, targetCount = targetCount, category = category))
    }

    suspend fun toggleGoal(goalId: Long, isCompleted: Boolean) {
        goalDao.toggleGoalCompletion(goalId, isCompleted)
    }

    suspend fun deleteGoal(goalId: Long) {
        goalDao.deleteGoal(goalId)
    }

    // Focus Rooms Co-Studying
    fun createFocusRoom(roomName: String, subject: String, targetMinutes: Int, ambientAudio: String): FocusRoom {
        val newRoom = FocusRoom(
            id = "room_${System.currentTimeMillis()}",
            roomName = roomName,
            subject = subject,
            hostName = "Sharif (You)",
            participantNames = listOf("Sharif (You)"),
            targetMinutes = targetMinutes,
            ambientAudioName = ambientAudio,
            isActive = true
        )
        _focusRoomsState.update { listOf(newRoom) + it }
        _activeUserRoom.value = newRoom
        
        // Broadcast room creation in chat
        externalScope.launch(Dispatchers.IO) {
            chatDao.insertMessage(ChatMessage(
                senderId = "user",
                senderName = "Sharif (You)",
                text = "🚀 I just started a Focus Room: $roomName ($subject). Click to join!",
                isNudge = true,
                nudgeType = "JOIN_ROOM"
            ))
        }
        return newRoom
    }

    fun joinFocusRoom(roomId: String) {
        var joinedRoom: FocusRoom? = null
        _focusRoomsState.update { rooms ->
            rooms.map { room ->
                if (room.id == roomId) {
                    val updatedParticipants = if (room.participantNames.contains("Sharif (You)")) room.participantNames else room.participantNames + "Sharif (You)"
                    val r = room.copy(
                        participantNames = updatedParticipants
                    )
                    joinedRoom = r
                    r
                } else room
            }
        }
        _activeUserRoom.value = joinedRoom
    }

    fun leaveCurrentFocusRoom() {
        val current = _activeUserRoom.value
        if (current != null) {
            _focusRoomsState.update { rooms ->
                rooms.map { room ->
                    if (room.id == current.id) {
                        room.copy(
                            participantNames = room.participantNames.filter { it != "Sharif (You)" }
                        )
                    } else room
                }
            }
            _activeUserRoom.value = null
        }
    }

    // Chat & Nudges
    suspend fun sendChatMessage(text: String) {
        val msg = ChatMessage(
            senderId = "user",
            senderName = "Sharif (You)",
            text = text,
            isFromUser = true,
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertMessage(msg)

        // Trigger dynamic squad response after delay
        externalScope.launch(Dispatchers.IO) {
            delay(2000L)
            triggerSquadAutoReply(text)
        }
    }

    suspend fun sendNudge(friendName: String, nudgeType: String) {
        val text = when (nudgeType) {
            "GET_TO_WORK" -> "⚡ Hey $friendName, time to open those books and start studying!"
            "KEEP_GOING" -> "🔥 Great focus $friendName! Keep going, SSC 2027 is ours!"
            "GREAT_JOB" -> "🎉 Awesome study session $friendName! Hard work pays off."
            else -> "📢 Hey $friendName, study nudge from Sharif!"
        }
        val msg = ChatMessage(
            senderId = "user",
            senderName = "Sharif (You)",
            text = text,
            isNudge = true,
            nudgeType = nudgeType,
            isFromUser = true,
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertMessage(msg)

        externalScope.launch(Dispatchers.IO) {
            delay(1800L)
            val replyText = when (nudgeType) {
                "GET_TO_WORK" -> "Thanks for the nudge, Sharif! Opening my Higher Math notebook now 📚"
                "KEEP_GOING" -> "Appreciate it brother! 2 hours down, 1 hour to go 💪"
                "GREAT_JOB" -> "Thanks Sharif! Let's crush today's goal together!"
                else -> "Got your nudge! Let me join your room!"
            }
            chatDao.insertMessage(ChatMessage(
                senderId = friendName.lowercase(),
                senderName = friendName,
                text = replyText,
                timestamp = System.currentTimeMillis()
            ))
        }
    }

    private suspend fun triggerSquadAutoReply(userText: String) {
        val lower = userText.lowercase()
        val (responder, replyText) = when {
            lower.contains("math") || lower.contains("physics") -> "Anik" to "Solving chapter 4 dynamics questions right now. Want to check solutions together?"
            lower.contains("room") || lower.contains("study") -> "Taskin" to "Joined! Let's lock in for a solid 45-minute sprint."
            lower.contains("ssc") || lower.contains("exam") -> "Nusrat" to "SSC 2027 countdown keeps me super motivated! Solving test papers every day."
            else -> "Sharif (Squad Leader)" to "Keep pushing squad! Consistency is key for our 2027 goals! 🚀"
        }
        chatDao.insertMessage(ChatMessage(
            senderId = responder.lowercase(),
            senderName = responder,
            text = replyText,
            timestamp = System.currentTimeMillis()
        ))
    }

    private fun initialMockFriends(): List<FriendItem> {
        return listOf(
            FriendItem(
                id = "f1",
                name = "Anik Rahman",
                studyFocus = "Higher Math & Physics Specialist",
                isOnline = true,
                status = "Studying",
                currentSubject = "Physics - Dynamics",
                studyMinutesToday = 145,
                avatarColorHex = 0xFF4F46E5,
                lastActiveTime = "Active now"
            ),
            FriendItem(
                id = "f2",
                name = "Taskin Ahmed",
                studyFocus = "SSC 2027 Science Syllabus",
                isOnline = true,
                status = "In Focus Room",
                currentSubject = "Higher Math - Integration",
                studyMinutesToday = 210,
                activeRoomId = "room_1",
                avatarColorHex = 0xFF06B6D4,
                lastActiveTime = "Active now"
            ),
            FriendItem(
                id = "f3",
                name = "Nusrat Jahan",
                studyFocus = "Biology & Chemistry Focus",
                isOnline = true,
                status = "Studying",
                currentSubject = "Chemistry - Organic",
                studyMinutesToday = 180,
                avatarColorHex = 0xFF10B981,
                lastActiveTime = "Active now"
            ),
            FriendItem(
                id = "f4",
                name = "Rahat Kabir",
                studyFocus = "SSC 2027 General Math",
                isOnline = false,
                status = "On Break",
                currentSubject = "General Math",
                studyMinutesToday = 90,
                avatarColorHex = 0xFFF59E0B,
                lastActiveTime = "12m ago"
            )
        )
    }

    private fun initialMockFocusRooms(): List<FocusRoom> {
        return listOf(
            FocusRoom(
                id = "room_1",
                roomName = "SSC 2027 Science Sprint 🚀",
                subject = "Higher Math & Physics",
                hostName = "Taskin Ahmed",
                hostAvatarHex = 0xFF06B6D4,
                participantNames = listOf("Taskin Ahmed", "Anik Rahman", "Nusrat Jahan"),
                elapsedSeconds = 1840L,
                targetMinutes = 60,
                isActive = true,
                ambientAudioName = "Deep Rain & Waves"
            ),
            FocusRoom(
                id = "room_2",
                roomName = "Organic Chemistry Quiet Library 📖",
                subject = "Chemistry",
                hostName = "Nusrat Jahan",
                hostAvatarHex = 0xFF10B981,
                participantNames = listOf("Nusrat Jahan"),
                elapsedSeconds = 920L,
                targetMinutes = 45,
                isActive = true,
                ambientAudioName = "Cafe White Noise"
            )
        )
    }
}
