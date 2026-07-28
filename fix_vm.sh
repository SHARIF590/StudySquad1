# Remove the bad tail from line 291 onwards and rewrite
head -n 290 app/src/main/java/com/example/viewmodel/StudySquadViewModel.kt > tmp_vm.kt
cat << 'INNER_EOF' >> tmp_vm.kt
    fun sendNudge(friendName: String, nudgeType: String) {
        viewModelScope.launch {
            repository.sendNudge(friendName, nudgeType)
        }
    }

    // Added for Analytics
    val allSessions: StateFlow<List<com.example.data.model.StudySession>> = repository.getAllSessionsFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addManualStudySession(subject: String, durationMinutes: Int, startTimeMillis: Long, endTimeMillis: Long) {
        viewModelScope.launch {
            repository.addManualStudySession(subject, durationMinutes, startTimeMillis, endTimeMillis)
        }
    }
}
INNER_EOF
mv tmp_vm.kt app/src/main/java/com/example/viewmodel/StudySquadViewModel.kt
