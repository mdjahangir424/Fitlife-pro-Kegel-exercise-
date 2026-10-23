package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ExerciseEntity
import com.example.data.model.ReminderSettingsEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)
}

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE category = :category")
    fun getWorkoutsByCategory(category: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getWorkoutById(id: String): WorkoutEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<WorkoutEntity>)

    @Update
    suspend fun update(workout: WorkoutEntity)

    @Query("UPDATE workouts SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean)
}

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId")
    fun getExercisesForWorkout(workoutId: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId")
    suspend fun getExercisesForWorkoutOnce(workoutId: String): List<ExerciseEntity>

    @Query("SELECT * FROM exercises WHERE isFavorite = 1")
    fun getFavoriteExercises(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE isDownloaded = 1")
    fun getDownloadedExercises(): Flow<List<ExerciseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<ExerciseEntity>)

    @Update
    suspend fun update(exercise: ExerciseEntity)

    @Query("UPDATE exercises SET isFavorite = :isFav WHERE id = :id")
    suspend fun toggleFavorite(id: String, isFav: Boolean)

    @Query("UPDATE exercises SET isDownloaded = :downloaded WHERE id = :id")
    suspend fun setDownloaded(id: String, downloaded: Boolean)
}

@Dao
interface WorkoutHistoryDao {
    @Query("SELECT * FROM workout_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<WorkoutHistoryEntity>>

    @Query("SELECT * FROM workout_history ORDER BY timestamp DESC LIMIT 20")
    suspend fun getRecentHistory(): List<WorkoutHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: WorkoutHistoryEntity)

    @Query("SELECT COUNT(*) FROM workout_history")
    fun getTotalWorkoutsCount(): Flow<Int>

    @Query("SELECT SUM(durationMinutes) FROM workout_history")
    fun getTotalMinutes(): Flow<Int?>

    @Query("DELETE FROM workout_history")
    suspend fun clearHistory()
}

@Dao
interface ReminderSettingsDao {
    @Query("SELECT * FROM reminder_settings WHERE id = 1")
    fun getReminderSettings(): Flow<ReminderSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminderSettings(settings: ReminderSettingsEntity)
}
