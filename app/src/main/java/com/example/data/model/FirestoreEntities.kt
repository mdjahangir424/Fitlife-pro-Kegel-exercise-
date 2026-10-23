package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreUserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val ageRange: String = "",
    val experienceLevel: String = "",
    val goal: String = "",
    val language: String = "bn",
    val hasCompletedOnboarding: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreWorkoutHistory(
    @DocumentId val id: String = "",
    val historyId: String = "",
    val userId: String = "",
    val workoutId: String = "",
    val workoutTitleBn: String = "",
    val workoutTitleEn: String = "",
    val durationMinutes: Long = 0L,
    val exercisesCompleted: Long = 0L,
    val totalExercises: Long = 0L,
    val caloriesBurned: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreFavorite(
    @DocumentId val id: String = "",
    val exerciseId: String = "",
    val userId: String = "",
    val titleBn: String = "",
    val titleEn: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
