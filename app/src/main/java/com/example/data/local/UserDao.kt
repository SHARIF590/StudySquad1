package com.example.data.local

import androidx.room.*
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfile)

    @Query("UPDATE user_profile SET currentStatus = :status, currentSubject = :subject WHERE id = 1")
    suspend fun updateStatus(status: String, subject: String)

    @Query("UPDATE user_profile SET totalStudyMinutes = totalStudyMinutes + :addMinutes WHERE id = 1")
    suspend fun addStudyTime(addMinutes: Long)
}
