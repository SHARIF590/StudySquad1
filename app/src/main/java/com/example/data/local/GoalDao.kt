package com.example.data.local

import androidx.room.*
import com.example.data.model.AcademicGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM academic_goals ORDER BY isCompleted ASC, id DESC")
    fun getAllGoals(): Flow<List<AcademicGoal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: AcademicGoal)

    @Update
    suspend fun updateGoal(goal: AcademicGoal)

    @Query("DELETE FROM academic_goals WHERE id = :id")
    suspend fun deleteGoal(id: Long)

    @Query("UPDATE academic_goals SET isCompleted = :completed, completedCount = CASE WHEN :completed THEN targetCount ELSE 0 END WHERE id = :id")
    suspend fun toggleGoalCompletion(id: Long, completed: Boolean)
}
