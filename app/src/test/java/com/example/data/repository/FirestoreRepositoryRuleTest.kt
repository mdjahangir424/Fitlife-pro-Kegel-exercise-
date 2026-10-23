package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.FirestoreUserProfile
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FirestoreRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun saveAndObserveUserProfile_authenticatedUser_success() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repository = FirestoreRepository(firestore)

    val profile = FirestoreUserProfile(
      userId = uid,
      name = "Alice Test",
      language = "bn",
      goal = "Pelvic Fitness"
    )
    val saveResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.saveUserProfile(profile)
    }
    assertTrue("Save profile should succeed", saveResult.isSuccess)

    val observed = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.observeUserProfile().first()
    }
    assertNotNull(observed)
    assertEquals(uid, observed?.userId)
    assertEquals("Alice Test", observed?.name)
  }

  @Test
  fun addAndObserveWorkoutHistory_authenticatedUser_success() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repository = FirestoreRepository(firestore)

    val addResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.addWorkoutHistory(
        workoutId = "kegel_workout_1",
        workoutTitleBn = "পেলভিক কেগেল",
        workoutTitleEn = "Pelvic Kegel",
        durationMinutes = 15,
        exercisesCompleted = 10,
        totalExercises = 10,
        caloriesBurned = 90
      )
    }
    assertTrue("Add workout history should succeed", addResult.isSuccess)
    val historyId = addResult.getOrThrow()
    assertTrue(historyId.isNotEmpty())

    val historyList = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.observeWorkoutHistory().first()
    }
    assertTrue(historyList.any { it.historyId == historyId })
  }

  @Test
  fun toggleAndObserveFavorites_authenticatedUser_success() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repository = FirestoreRepository(firestore)

    val toggleResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.toggleFavorite("kegel_ex_1", "ব্রিজিং", "Bridging")
    }
    assertTrue("Toggle favorite should succeed", toggleResult.isSuccess)
    assertTrue("Should be favorited", toggleResult.getOrThrow())

    val favorites = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.observeFavorites().first()
    }
    assertTrue(favorites.any { it.exerciseId == "kegel_ex_1" })
  }

  @Test
  fun unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
    auth.signOut()
    var caughtException: Exception? = null
    try {
      withTimeout(FLOW_TIMEOUT_MS) {
        // Direct collection read without auth should be rejected
        firestore.collection("users").document("someone").collection("workout_history").get().await()
      }
    } catch (e: Exception) {
      caughtException = e
    }
    assertNotNull("Expected exception to be caught", caughtException)
    val firestoreException = caughtException?.unwrapFirestoreException()
    assertNotNull("Expected FirebaseFirestoreException", firestoreException)
    assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
  }

  private companion object {
    const val ALICE_EMAIL = "alice_test@example.com"
    const val DEFAULT_TIMEOUT_MS = 6000L
    const val FLOW_TIMEOUT_MS = 4000L
  }
}
