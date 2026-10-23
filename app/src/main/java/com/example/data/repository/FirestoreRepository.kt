package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.FirestoreFavorite
import com.example.data.model.FirestoreUserProfile
import com.example.data.model.FirestoreWorkoutHistory
import com.example.util.OperationType
import com.example.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth get() = Firebase.auth

    fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    val currentUserId: String?
        get() = auth.currentUser?.uid

    // --- User Profile ---

    fun observeUserProfile(): Flow<FirestoreUserProfile?> = flow {
        val uid = requireUserId()
        val path = "users/$uid"
        val docRef = db.collection("users").document(uid)
        emitAll(
            docRef.snapshots()
                .map { snapshot ->
                    if (snapshot.exists()) {
                        snapshot.toObject(FirestoreUserProfile::class.java)
                    } else {
                        null
                    }
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.GET, path)
                    throw error
                }
        )
    }

    suspend fun saveUserProfile(profile: FirestoreUserProfile): Result<Unit> {
        val uid = requireUserId()
        val path = "users/$uid"
        val docRef = db.collection("users").document(uid)
        return try {
            val existingSnapshot = docRef.get().await()
            val payload = mutableMapOf<String, Any>(
                "userId" to uid,
                "name" to profile.name,
                "ageRange" to profile.ageRange,
                "experienceLevel" to profile.experienceLevel,
                "goal" to profile.goal,
                "language" to profile.language,
                "hasCompletedOnboarding" to profile.hasCompletedOnboarding,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (!existingSnapshot.exists()) {
                payload["createdAt"] = FieldValue.serverTimestamp()
            }
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    // --- Workout History ---

    fun observeWorkoutHistory(): Flow<List<FirestoreWorkoutHistory>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/workout_history"
        val colRef = db.collection("users").document(uid).collection("workout_history")
        emitAll(
            colRef.snapshots()
                .map { snapshot ->
                    snapshot.toObjects(FirestoreWorkoutHistory::class.java)
                        .sortedByDescending { it.createdAt?.toDate()?.time ?: 0L }
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun addWorkoutHistory(
        workoutId: String,
        workoutTitleBn: String,
        workoutTitleEn: String,
        durationMinutes: Long,
        exercisesCompleted: Long,
        totalExercises: Long,
        caloriesBurned: Long
    ): Result<String> {
        val uid = requireUserId()
        val historyId = UUID.randomUUID().toString()
        val path = "users/$uid/workout_history/$historyId"
        val docRef = db.collection("users").document(uid).collection("workout_history").document(historyId)
        return try {
            val payload = mapOf(
                "historyId" to historyId,
                "userId" to uid,
                "workoutId" to workoutId,
                "workoutTitleBn" to workoutTitleBn,
                "workoutTitleEn" to workoutTitleEn,
                "durationMinutes" to durationMinutes,
                "exercisesCompleted" to exercisesCompleted,
                "totalExercises" to totalExercises,
                "caloriesBurned" to caloriesBurned,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(historyId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    // --- Favorites ---

    fun observeFavorites(): Flow<List<FirestoreFavorite>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/favorites"
        val colRef = db.collection("users").document(uid).collection("favorites")
        emitAll(
            colRef.snapshots()
                .map { snapshot ->
                    snapshot.toObjects(FirestoreFavorite::class.java)
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun toggleFavorite(exerciseId: String, titleBn: String, titleEn: String): Result<Boolean> {
        val uid = requireUserId()
        val path = "users/$uid/favorites/$exerciseId"
        val docRef = db.collection("users").document(uid).collection("favorites").document(exerciseId)
        return try {
            val snapshot = docRef.get().await()
            if (snapshot.exists()) {
                docRef.delete().await()
                Result.success(false)
            } else {
                val payload = mapOf(
                    "exerciseId" to exerciseId,
                    "userId" to uid,
                    "titleBn" to titleBn,
                    "titleEn" to titleEn,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
                Result.success(true)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }
}
