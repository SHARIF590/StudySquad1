# Remove the messed up end
head -n 461 app/src/main/java/com/example/data/repository/StudySquadRepository.kt > temp_repo.kt
cat << 'INNER_EOF' >> temp_repo.kt
    }

    // Added manually
    fun getAllSessionsFlow(): Flow<List<StudySession>> = studySessionDao.getAllSessions()

    suspend fun addManualStudySession(subject: String, durationMinutes: Int, startTimeMillis: Long, endTimeMillis: Long) {
        val session = StudySession(
            category = subject,
            startTime = startTimeMillis,
            endTime = endTimeMillis,
            duration = durationMinutes,
            isManualEntry = true,
            pomodorosCompleted = 0
        )
        studySessionDao.insertSession(session)
        
        // Update streak based on the new session's date
        val profile = userDao.getUserProfileOnce()
        if (profile != null) {
            val lastDate = profile.lastStudyDate
            val now = System.currentTimeMillis()
            
            val calendarNow = java.util.Calendar.getInstance().apply { timeInMillis = now }
            val calendarLast = java.util.Calendar.getInstance().apply { timeInMillis = lastDate }
            
            val diffDays = calculateDaysBetween(calendarLast, calendarNow)
            
            val newStreak = when {
                lastDate == 0L -> 1
                diffDays == 1 -> profile.currentStreak + 1
                diffDays > 1 -> 1
                else -> profile.currentStreak
            }
            
            userDao.saveUserProfile(
                profile.copy(
                    currentStreak = newStreak,
                    lastStudyDate = now, // simplified, assuming manual entry counts as activity today
                    totalStudyMinutes = profile.totalStudyMinutes + durationMinutes
                )
            )
        }
    }
}
INNER_EOF
mv temp_repo.kt app/src/main/java/com/example/data/repository/StudySquadRepository.kt
