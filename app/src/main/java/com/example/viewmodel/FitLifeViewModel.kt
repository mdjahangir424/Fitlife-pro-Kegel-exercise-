package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FitLifeDatabase
import com.example.data.FitLifeRepository
import com.example.data.model.ExerciseEntity
import com.example.data.model.FirestoreUserProfile
import com.example.data.model.ReminderSettingsEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutHistoryEntity
import com.example.data.repository.FirestoreRepository
import com.example.ui.theme.AppThemeMode
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    SPLASH,
    ONBOARDING,
    LANGUAGE_SELECTION,
    PROFILE_SETUP,
    HOME,
    CATEGORIES,
    PROGRESS,
    PROFILE,
    WORKOUT_DETAIL,
    ACTIVE_WORKOUT,
    REST_TIMER,
    WORKOUT_COMPLETION,
    CALENDAR,
    HISTORY,
    DOWNLOADS,
    FAVORITES,
    REMINDERS,
    ABOUT_OWNER,
    DATABASE_MANAGER,
    EXERCISE_VIDEO_PLAYER
}

class FitLifeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FitLifeRepository
    private var firestoreRepo: FirestoreRepository? = null

    // Cloud sync state
    private val _cloudSyncStatus = MutableStateFlow("Synced")
    val cloudSyncStatus: StateFlow<String> = _cloudSyncStatus.asStateFlow()

    private val _currentUserEmail = MutableStateFlow<String?>(null)
    val currentUserEmail: StateFlow<String?> = _currentUserEmail.asStateFlow()

    init {
        val database = FitLifeDatabase.getDatabase(application)
        repository = FitLifeRepository(database)
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        // Initialize Firestore repository if already authenticated
        try {
            val currentFirebaseUser = Firebase.auth.currentUser
            if (currentFirebaseUser != null) {
                onAuthStateChanged(currentFirebaseUser)
            }
        } catch (e: Throwable) {
            _cloudSyncStatus.value = "Offline Mode"
        }
    }

    fun onAuthStateChanged(user: FirebaseUser?) {
        if (user != null) {
            _currentUserEmail.value = user.email ?: user.displayName ?: "Google User"
            try {
                val repo = FirestoreRepository(getApplication())
                firestoreRepo = repo
                _cloudSyncStatus.value = "Synced"

                // Observe and sync user profile with Firestore
            viewModelScope.launch {
                try {
                    repo.observeUserProfile().collect { cloudProfile ->
                        if (cloudProfile != null) {
                            repository.saveProfile(
                                UserProfileEntity(
                                    id = 1,
                                    name = cloudProfile.name.ifEmpty { "মোঃ জাহাঙ্গীর আলম" },
                                    ageRange = cloudProfile.ageRange.ifEmpty { "25 - 35" },
                                    experienceLevel = cloudProfile.experienceLevel.ifEmpty { "Beginner" },
                                    goal = cloudProfile.goal.ifEmpty { "General Fitness" },
                                    language = cloudProfile.language.ifEmpty { "bn" },
                                    hasCompletedOnboarding = cloudProfile.hasCompletedOnboarding
                                )
                            )
                        } else {
                            // Upload local profile to Firestore
                            val local = repository.userProfile.stateIn(viewModelScope).value
                            if (local != null) {
                                repo.saveUserProfile(
                                    FirestoreUserProfile(
                                        userId = user.uid,
                                        name = local.name,
                                        ageRange = local.ageRange,
                                        experienceLevel = local.experienceLevel,
                                        goal = local.goal,
                                        language = local.language,
                                        hasCompletedOnboarding = local.hasCompletedOnboarding
                                    )
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    _cloudSyncStatus.value = "Offline"
                }
            }
            } catch (e: Exception) {
                firestoreRepo = null
                _cloudSyncStatus.value = "Offline"
            }
        } else {
            _currentUserEmail.value = null
            firestoreRepo = null
            _cloudSyncStatus.value = "Signed Out"
        }
    }

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allWorkouts: StateFlow<List<WorkoutEntity>> = repository.allWorkouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteExercises: StateFlow<List<ExerciseEntity>> = repository.favoriteExercises
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val downloadedExercises: StateFlow<List<ExerciseEntity>> = repository.downloadedExercises
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workoutHistory: StateFlow<List<WorkoutHistoryEntity>> = repository.workoutHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reminderSettings: StateFlow<ReminderSettingsEntity?> = repository.reminderSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val totalWorkoutsCount: StateFlow<Int> = repository.totalWorkoutsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMinutes: StateFlow<Int?> = repository.totalMinutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Navigation state
    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<Screen>()

    // Current selected workout
    private val _selectedWorkout = MutableStateFlow<WorkoutEntity?>(null)
    val selectedWorkout: StateFlow<WorkoutEntity?> = _selectedWorkout.asStateFlow()

    private val _selectedWorkoutExercises = MutableStateFlow<List<ExerciseEntity>>(emptyList())
    val selectedWorkoutExercises: StateFlow<List<ExerciseEntity>> = _selectedWorkoutExercises.asStateFlow()

    private val _videoExercise = MutableStateFlow<ExerciseEntity?>(null)
    val videoExercise: StateFlow<ExerciseEntity?> = _videoExercise.asStateFlow()

    fun openVideoPlayer(exercise: ExerciseEntity) {
        _videoExercise.value = exercise
        navigateTo(Screen.EXERCISE_VIDEO_PLAYER)
    }

    // Active workout session state
    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex.asStateFlow()

    private val _timerSeconds = MutableStateFlow(30)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()

    private val _totalExerciseSeconds = MutableStateFlow(30)
    val totalExerciseSeconds: StateFlow<Int> = _totalExerciseSeconds.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _restSeconds = MutableStateFlow(15)
    val restSeconds: StateFlow<Int> = _restSeconds.asStateFlow()

    // Last completed session stats
    private val _completedDurationMinutes = MutableStateFlow(20)
    val completedDurationMinutes: StateFlow<Int> = _completedDurationMinutes.asStateFlow()

    private val _completedExercisesCount = MutableStateFlow(10)
    val completedExercisesCount: StateFlow<Int> = _completedExercisesCount.asStateFlow()

    private val _completedCalories = MutableStateFlow(110)
    val completedCalories: StateFlow<Int> = _completedCalories.asStateFlow()

    // JSON export / ownership state
    private val _exportedJson = MutableStateFlow("")
    val exportedJson: StateFlow<String> = _exportedJson.asStateFlow()

    // Theme Mode state (DARK, LIGHT, SYSTEM)
    private val _themeMode = MutableStateFlow(AppThemeMode.DARK)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
    }

    fun toggleThemeMode() {
        _themeMode.value = when (_themeMode.value) {
            AppThemeMode.DARK -> AppThemeMode.LIGHT
            AppThemeMode.LIGHT -> AppThemeMode.SYSTEM
            AppThemeMode.SYSTEM -> AppThemeMode.DARK
        }
    }

    // Search query for categories
    private val _categorySearch = MutableStateFlow("")
    val categorySearch: StateFlow<String> = _categorySearch.asStateFlow()

    fun setCategorySearch(query: String) {
        _categorySearch.value = query
    }

    private var timerJob: Job? = null

    fun navigateTo(screen: Screen, addToBackStack: Boolean = true) {
        if (addToBackStack) {
            screenBackStack.add(_currentScreen.value)
        }
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        return if (screenBackStack.isNotEmpty()) {
            val previous = screenBackStack.removeAt(screenBackStack.size - 1)
            _currentScreen.value = previous
            true
        } else {
            false
        }
    }

    fun selectWorkout(workout: WorkoutEntity) {
        _selectedWorkout.value = workout
        viewModelScope.launch {
            repository.getExercisesForWorkout(workout.id).collect { list ->
                _selectedWorkoutExercises.value = list
            }
        }
    }

    fun startWorkoutSession(workout: WorkoutEntity) {
        selectWorkout(workout)
        viewModelScope.launch {
            repository.getExercisesForWorkout(workout.id).collect { list ->
                if (list.isNotEmpty()) {
                    _selectedWorkoutExercises.value = list
                    _currentExerciseIndex.value = 0
                    val firstEx = list[0]
                    _timerSeconds.value = firstEx.durationSec
                    _totalExerciseSeconds.value = firstEx.durationSec
                    _isPaused.value = false
                    navigateTo(Screen.ACTIVE_WORKOUT, addToBackStack = false)
                    startActiveExerciseTimer()
                }
            }
        }
    }

    fun togglePauseTimer() {
        _isPaused.value = !_isPaused.value
    }

    fun skipCurrentExercise() {
        timerJob?.cancel()
        val nextIndex = _currentExerciseIndex.value + 1
        val exercises = _selectedWorkoutExercises.value
        if (nextIndex < exercises.size) {
            _restSeconds.value = 15
            navigateTo(Screen.REST_TIMER, addToBackStack = false)
            startRestTimer(nextIndex)
        } else {
            finishWorkoutSession()
        }
    }

    private fun startActiveExerciseTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerSeconds.value > 0) {
                delay(1000)
                if (!_isPaused.value) {
                    _timerSeconds.value -= 1
                }
            }
            // Exercise complete, go to rest or finish
            val nextIndex = _currentExerciseIndex.value + 1
            val exercises = _selectedWorkoutExercises.value
            if (nextIndex < exercises.size) {
                val currentEx = exercises[_currentExerciseIndex.value]
                _restSeconds.value = currentEx.restSec
                navigateTo(Screen.REST_TIMER, addToBackStack = false)
                startRestTimer(nextIndex)
            } else {
                finishWorkoutSession()
            }
        }
    }

    private fun startRestTimer(nextExerciseIndex: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_restSeconds.value > 0) {
                delay(1000)
                _restSeconds.value -= 1
            }
            // Go to next exercise
            proceedToExercise(nextExerciseIndex)
        }
    }

    fun skipRest() {
        timerJob?.cancel()
        val nextIndex = _currentExerciseIndex.value + 1
        proceedToExercise(nextIndex)
    }

    private fun proceedToExercise(index: Int) {
        val exercises = _selectedWorkoutExercises.value
        if (index < exercises.size) {
            _currentExerciseIndex.value = index
            val ex = exercises[index]
            _timerSeconds.value = ex.durationSec
            _totalExerciseSeconds.value = ex.durationSec
            _isPaused.value = false
            navigateTo(Screen.ACTIVE_WORKOUT, addToBackStack = false)
            startActiveExerciseTimer()
        } else {
            finishWorkoutSession()
        }
    }

    fun finishWorkoutSession() {
        timerJob?.cancel()
        val workout = _selectedWorkout.value
        val exCount = _selectedWorkoutExercises.value.size
        val duration = workout?.durationMinutes ?: 30
        val calories = (duration * 7.5).toInt()

        _completedDurationMinutes.value = duration
        _completedExercisesCount.value = exCount
        _completedCalories.value = calories

        viewModelScope.launch {
            workout?.let {
                // Save locally to Room
                repository.logCompletedWorkout(
                    workoutId = it.id,
                    titleBn = it.titleBn,
                    titleEn = it.titleEn,
                    durationMinutes = duration,
                    exercisesCompleted = exCount,
                    totalExercises = exCount,
                    calories = calories
                )

                // Sync to Firestore Cloud
                firestoreRepo?.let { fRepo ->
                    fRepo.addWorkoutHistory(
                        workoutId = it.id,
                        workoutTitleBn = it.titleBn,
                        workoutTitleEn = it.titleEn,
                        durationMinutes = duration.toLong(),
                        exercisesCompleted = exCount.toLong(),
                        totalExercises = exCount.toLong(),
                        caloriesBurned = calories.toLong()
                    )
                }
            }
        }
        navigateTo(Screen.WORKOUT_COMPLETION, addToBackStack = false)
    }

    fun toggleExerciseFavorite(exerciseId: String, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavoriteExercise(exerciseId, !currentFav)
            // Sync to Firestore Cloud
            firestoreRepo?.let { fRepo ->
                fRepo.toggleFavorite(exerciseId, "পেলভিক কেগেল এক্সারসাইজ", "Pelvic Kegel Exercise")
            }
        }
    }

    fun toggleExerciseDownload(exerciseId: String, isDownloaded: Boolean) {
        viewModelScope.launch {
            repository.toggleDownloadExercise(exerciseId, !isDownloaded)
        }
    }

    fun updateProfile(name: String, age: String, exp: String, goal: String, lang: String) {
        viewModelScope.launch {
            repository.saveProfile(
                UserProfileEntity(
                    id = 1,
                    name = name,
                    ageRange = age,
                    experienceLevel = exp,
                    goal = goal,
                    language = lang,
                    hasCompletedOnboarding = true
                )
            )

            // Sync to Firestore
            firestoreRepo?.let { fRepo ->
                val uid = fRepo.currentUserId ?: return@launch
                fRepo.saveUserProfile(
                    FirestoreUserProfile(
                        userId = uid,
                        name = name,
                        ageRange = age,
                        experienceLevel = exp,
                        goal = goal,
                        language = lang,
                        hasCompletedOnboarding = true
                    )
                )
            }
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            val curr = repository.userProfile.stateIn(viewModelScope).value
            if (curr != null) {
                repository.saveProfile(curr.copy(language = lang))
            } else {
                repository.saveProfile(UserProfileEntity(id = 1, language = lang))
            }

            // Sync to Firestore
            firestoreRepo?.let { fRepo ->
                val uid = fRepo.currentUserId ?: return@launch
                val currProfile = curr ?: UserProfileEntity(id = 1, language = lang)
                fRepo.saveUserProfile(
                    FirestoreUserProfile(
                        userId = uid,
                        name = currProfile.name,
                        ageRange = currProfile.ageRange,
                        experienceLevel = currProfile.experienceLevel,
                        goal = currProfile.goal,
                        language = lang,
                        hasCompletedOnboarding = currProfile.hasCompletedOnboarding
                    )
                )
            }
        }
    }

    fun toggleLanguage() {
        val currLang = userProfile.value?.language ?: "bn"
        val nextLang = if (currLang == "bn") "en" else "bn"
        setLanguage(nextLang)
    }

    fun saveReminder(enabled: Boolean, time: String, days: String) {
        viewModelScope.launch {
            repository.saveReminder(
                ReminderSettingsEntity(
                    id = 1,
                    isEnabled = enabled,
                    reminderTime = time,
                    selectedDays = days
                )
            )
        }
    }

    fun generateDatabaseExport() {
        viewModelScope.launch {
            val json = repository.exportAllDataAsJson()
            _exportedJson.value = json
        }
    }
}
