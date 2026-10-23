package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FitLifeDatabase
import com.example.data.FitLifeRepository
import com.example.data.model.ExerciseEntity
import com.example.data.model.ReminderSettingsEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutHistoryEntity
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
    DATABASE_MANAGER
}

class FitLifeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FitLifeRepository

    init {
        val database = FitLifeDatabase.getDatabase(application)
        repository = FitLifeRepository(database)
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
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
    private val _completedDurationMinutes = MutableStateFlow(30)
    val completedDurationMinutes: StateFlow<Int> = _completedDurationMinutes.asStateFlow()

    private val _completedExercisesCount = MutableStateFlow(12)
    val completedExercisesCount: StateFlow<Int> = _completedExercisesCount.asStateFlow()

    private val _completedCalories = MutableStateFlow(210)
    val completedCalories: StateFlow<Int> = _completedCalories.asStateFlow()

    // JSON export / ownership state
    private val _exportedJson = MutableStateFlow("")
    val exportedJson: StateFlow<String> = _exportedJson.asStateFlow()

    // Search query for categories
    private val _categorySearch = MutableStateFlow("")
    val categorySearch: StateFlow<String> = _categorySearch.asStateFlow()

    private var timerJob: Job? = null

    fun navigateTo(screen: Screen, addToBackStack: Boolean = true) {
        if (addToBackStack && _currentScreen.value != screen) {
            screenBackStack.add(_currentScreen.value)
        }
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (screenBackStack.isNotEmpty()) {
            _currentScreen.value = screenBackStack.removeAt(screenBackStack.size - 1)
            return true
        } else if (_currentScreen.value != Screen.HOME) {
            _currentScreen.value = Screen.HOME
            return true
        }
        return false
    }

    fun setCategorySearch(query: String) {
        _categorySearch.value = query
    }

    fun selectWorkout(workout: WorkoutEntity) {
        _selectedWorkout.value = workout
        viewModelScope.launch {
            val exercises = repository.getExercisesForWorkoutOnce(workout.id)
            _selectedWorkoutExercises.value = exercises
        }
    }

    fun startWorkoutSession(workout: WorkoutEntity) {
        selectWorkout(workout)
        viewModelScope.launch {
            val exercises = repository.getExercisesForWorkoutOnce(workout.id)
            if (exercises.isNotEmpty()) {
                _selectedWorkoutExercises.value = exercises
                _currentExerciseIndex.value = 0
                val firstEx = exercises[0]
                _timerSeconds.value = firstEx.durationSec
                _totalExerciseSeconds.value = firstEx.durationSec
                _isPaused.value = false
                navigateTo(Screen.ACTIVE_WORKOUT)
                startActiveExerciseTimer()
            }
        }
    }

    private fun startActiveExerciseTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerSeconds.value > 0) {
                if (!_isPaused.value) {
                    delay(1000)
                    _timerSeconds.value -= 1
                } else {
                    delay(300)
                }
            }
            // Finished current exercise
            onCurrentExerciseFinished()
        }
    }

    fun togglePauseTimer() {
        _isPaused.value = !_isPaused.value
    }

    fun skipCurrentExercise() {
        onCurrentExerciseFinished()
    }

    private fun onCurrentExerciseFinished() {
        timerJob?.cancel()
        val nextIndex = _currentExerciseIndex.value + 1
        val exercises = _selectedWorkoutExercises.value
        if (nextIndex < exercises.size) {
            // Show rest screen
            val currentEx = exercises[_currentExerciseIndex.value]
            _restSeconds.value = currentEx.restSec
            navigateTo(Screen.REST_TIMER, addToBackStack = false)
            startRestTimer(nextIndex)
        } else {
            // Workout finished completely!
            finishWorkoutSession()
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
                repository.logCompletedWorkout(
                    workoutId = it.id,
                    titleBn = it.titleBn,
                    titleEn = it.titleEn,
                    durationMinutes = duration,
                    exercisesCompleted = exCount,
                    totalExercises = exCount,
                    calories = calories
                )
            }
        }
        navigateTo(Screen.WORKOUT_COMPLETION, addToBackStack = false)
    }

    fun toggleExerciseFavorite(exerciseId: String, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavoriteExercise(exerciseId, !currentFav)
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
        }
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
            _exportedJson.value = repository.exportDatabaseToJson()
        }
    }
}
