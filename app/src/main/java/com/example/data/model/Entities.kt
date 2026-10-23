package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "মোঃ জাহাঙ্গীর আলম",
    val ageRange: String = "25 - 35",
    val experienceLevel: String = "Beginner",
    val goal: String = "General Fitness",
    val language: String = "bn", // "bn" or "en"
    val hasCompletedOnboarding: Boolean = true
)

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val id: String,
    val titleBn: String,
    val titleEn: String,
    val category: String, // "full_body", "abs", "chest", "arms", "shoulders", "back", "legs", "stretching"
    val durationMinutes: Int,
    val exerciseCount: Int,
    val levelBn: String,
    val levelEn: String,
    val descriptionBn: String,
    val descriptionEn: String,
    val colorHex: String,
    val isFavorite: Boolean = false
)

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val workoutId: String,
    val titleBn: String,
    val titleEn: String,
    val targetBn: String,
    val targetEn: String,
    val durationSec: Int,
    val restSec: Int,
    val animationType: String, // "push_up", "plank", "squat", "mountain_climber", "lunges", "jumping_jacks", "crunches", "stretching"
    val instructionsBn: String,
    val instructionsEn: String,
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = true,
    val downloadSizeMb: Double = 12.5
)

@Entity(tableName = "workout_history")
data class WorkoutHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutId: String,
    val workoutTitleBn: String,
    val workoutTitleEn: String,
    val timestamp: Long = System.currentTimeMillis(),
    val durationMinutes: Int,
    val exercisesCompleted: Int,
    val totalExercises: Int,
    val caloriesBurned: Int
)

@Entity(tableName = "reminder_settings")
data class ReminderSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val isEnabled: Boolean = true,
    val reminderTime: String = "07:00 AM",
    val selectedDays: String = "Mon,Tue,Wed,Thu,Fri,Sat,Sun"
)
