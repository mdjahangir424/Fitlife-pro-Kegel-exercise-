package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.util.LanguageStrings
import com.example.viewmodel.FitLifeViewModel
import com.example.viewmodel.Screen

@Composable
fun MainContainerScreen(viewModel: FitLifeViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val isBangla = (userProfile?.language ?: "bn") == "bn"
    val currentUserEmail by viewModel.currentUserEmail.collectAsState()

    val allWorkouts by viewModel.allWorkouts.collectAsState()
    val selectedWorkout by viewModel.selectedWorkout.collectAsState()
    val selectedExercises by viewModel.selectedWorkoutExercises.collectAsState()
    val videoExercise by viewModel.videoExercise.collectAsState()

    val currentExIndex by viewModel.currentExerciseIndex.collectAsState()
    val timerSec by viewModel.timerSeconds.collectAsState()
    val totalSec by viewModel.totalExerciseSeconds.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()
    val restSec by viewModel.restSeconds.collectAsState()

    val completedDuration by viewModel.completedDurationMinutes.collectAsState()
    val completedCount by viewModel.completedExercisesCount.collectAsState()
    val completedCalories by viewModel.completedCalories.collectAsState()

    val workoutHistory by viewModel.workoutHistory.collectAsState()
    val totalWorkouts by viewModel.totalWorkoutsCount.collectAsState()
    val totalMins by viewModel.totalMinutes.collectAsState()
    val favoriteExercises by viewModel.favoriteExercises.collectAsState()
    val downloadedExercises by viewModel.downloadedExercises.collectAsState()
    val reminderSettings by viewModel.reminderSettings.collectAsState()
    val exportedJson by viewModel.exportedJson.collectAsState()
    val categorySearch by viewModel.categorySearch.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()

    val isMainTab = currentScreen in listOf(
        Screen.HOME,
        Screen.CATEGORIES,
        Screen.PROGRESS,
        Screen.PROFILE,
        Screen.LANGUAGE_SELECTION
    )

    // Back handling: If on secondary tab, back button returns to Home
    if (isMainTab && currentScreen != Screen.HOME) {
        BackHandler {
            viewModel.navigateTo(Screen.HOME, addToBackStack = false)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (isMainTab) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                ) {
                    // 1. Home Tab
                    NavigationBarItem(
                        selected = currentScreen == Screen.HOME,
                        onClick = { viewModel.navigateTo(Screen.HOME, addToBackStack = false) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = LanguageStrings.get("nav_home", isBangla),
                                fontSize = 10.sp,
                                fontWeight = if (currentScreen == Screen.HOME) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = SkyBlueLight,
                            indicatorColor = SkyBluePrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    // 2. Categories Tab
                    NavigationBarItem(
                        selected = currentScreen == Screen.CATEGORIES,
                        onClick = { viewModel.navigateTo(Screen.CATEGORIES, addToBackStack = false) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Category,
                                contentDescription = "Categories",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = LanguageStrings.get("nav_categories", isBangla),
                                fontSize = 10.sp,
                                fontWeight = if (currentScreen == Screen.CATEGORIES) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = SkyBlueLight,
                            indicatorColor = SkyBluePrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_categories")
                    )

                    // 3. Progress Tab
                    NavigationBarItem(
                        selected = currentScreen == Screen.PROGRESS,
                        onClick = { viewModel.navigateTo(Screen.PROGRESS, addToBackStack = false) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Progress",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = LanguageStrings.get("nav_progress", isBangla),
                                fontSize = 10.sp,
                                fontWeight = if (currentScreen == Screen.PROGRESS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = SkyBlueLight,
                            indicatorColor = SkyBluePrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_progress")
                    )

                    // 4. Dedicated Profile Setup Tab
                    NavigationBarItem(
                        selected = currentScreen == Screen.PROFILE,
                        onClick = { viewModel.navigateTo(Screen.PROFILE, addToBackStack = false) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = LanguageStrings.get("nav_profile", isBangla),
                                fontSize = 10.sp,
                                fontWeight = if (currentScreen == Screen.PROFILE) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = SkyBlueLight,
                            indicatorColor = SkyBluePrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_profile")
                    )

                    // 5. Dedicated Language Options Tab
                    NavigationBarItem(
                        selected = currentScreen == Screen.LANGUAGE_SELECTION,
                        onClick = { viewModel.navigateTo(Screen.LANGUAGE_SELECTION, addToBackStack = false) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Language",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = LanguageStrings.get("nav_language", isBangla),
                                fontSize = 10.sp,
                                fontWeight = if (currentScreen == Screen.LANGUAGE_SELECTION) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = SkyBlueLight,
                            indicatorColor = SkyBluePrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_language")
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentScreen) {
                Screen.SPLASH -> {
                    SplashScreen(
                        isBangla = isBangla,
                        onFinish = {
                            if (userProfile?.hasCompletedOnboarding == true) {
                                viewModel.navigateTo(Screen.HOME, addToBackStack = false)
                            } else {
                                viewModel.navigateTo(Screen.ONBOARDING, addToBackStack = false)
                            }
                        }
                    )
                }

                Screen.ONBOARDING -> {
                    OnboardingScreen(
                        isBangla = isBangla,
                        onStart = { viewModel.navigateTo(Screen.LANGUAGE_SELECTION) },
                        onSkip = { viewModel.navigateTo(Screen.HOME, addToBackStack = false) }
                    )
                }

                Screen.LANGUAGE_SELECTION -> {
                    LanguageScreen(
                        currentLanguage = userProfile?.language ?: "bn",
                        themeMode = themeMode,
                        onLanguageSelected = { lang -> viewModel.setLanguage(lang) },
                        onToggleTheme = { mode -> viewModel.setThemeMode(mode) },
                        onContinue = { viewModel.navigateTo(Screen.PROFILE_SETUP) },
                        onGoToProfile = { viewModel.navigateTo(Screen.PROFILE, addToBackStack = false) },
                        isTabMode = isMainTab
                    )
                }

                Screen.PROFILE_SETUP -> {
                    ProfileSetupScreen(
                        initialName = userProfile?.name ?: "",
                        initialAge = userProfile?.ageRange ?: "25 - 35",
                        initialExp = userProfile?.experienceLevel ?: "Beginner",
                        initialGoal = userProfile?.goal ?: "General Fitness",
                        isBangla = isBangla,
                        currentUserEmail = currentUserEmail,
                        onSave = { name, age, exp, goal ->
                            viewModel.updateProfile(name, age, exp, goal, userProfile?.language ?: "bn")
                            viewModel.navigateTo(Screen.HOME, addToBackStack = false)
                        },
                        onViewAboutOwner = { viewModel.navigateTo(Screen.ABOUT_OWNER) },
                        onNavigateLanguage = { viewModel.navigateTo(Screen.LANGUAGE_SELECTION, addToBackStack = false) },
                        isTabMode = false
                    )
                }

                Screen.PROFILE -> {
                    ProfileSetupScreen(
                        initialName = userProfile?.name ?: "",
                        initialAge = userProfile?.ageRange ?: "25 - 35",
                        initialExp = userProfile?.experienceLevel ?: "Beginner",
                        initialGoal = userProfile?.goal ?: "General Fitness",
                        isBangla = isBangla,
                        currentUserEmail = currentUserEmail,
                        onSave = { name, age, exp, goal ->
                            viewModel.updateProfile(name, age, exp, goal, userProfile?.language ?: "bn")
                        },
                        onViewAboutOwner = { viewModel.navigateTo(Screen.ABOUT_OWNER) },
                        onNavigateLanguage = { viewModel.navigateTo(Screen.LANGUAGE_SELECTION, addToBackStack = false) },
                        isTabMode = true
                    )
                }

                Screen.HOME -> {
                    HomeScreen(
                        userProfile = userProfile,
                        workouts = allWorkouts,
                        isBangla = isBangla,
                        themeMode = themeMode,
                        onWorkoutClick = { workout ->
                            viewModel.selectWorkout(workout)
                            viewModel.navigateTo(Screen.WORKOUT_DETAIL)
                        },
                        onStartWorkout = { workout ->
                            viewModel.startWorkoutSession(workout)
                        },
                        onNotificationClick = {
                            viewModel.navigateTo(Screen.REMINDERS)
                        },
                        onCalendarClick = {
                            viewModel.navigateTo(Screen.CALENDAR)
                        },
                        onToggleLanguage = {
                            viewModel.toggleLanguage()
                        },
                        onToggleTheme = {
                            viewModel.toggleThemeMode()
                        }
                    )
                }

                Screen.CATEGORIES -> {
                    CategoriesScreen(
                        workouts = allWorkouts,
                        searchQuery = categorySearch,
                        onSearchChange = { viewModel.setCategorySearch(it) },
                        isBangla = isBangla,
                        onCategoryClick = { workout ->
                            viewModel.selectWorkout(workout)
                            viewModel.navigateTo(Screen.WORKOUT_DETAIL)
                        }
                    )
                }

                Screen.PROGRESS -> {
                    ProgressScreen(
                        totalWorkouts = totalWorkouts,
                        totalMinutes = totalMins ?: 56,
                        isBangla = isBangla,
                        onViewHistory = {
                            viewModel.navigateTo(Screen.HISTORY)
                        }
                    )
                }

                Screen.ABOUT_OWNER -> {
                    AboutOwnerScreen(
                        isBangla = isBangla,
                        exportedJson = exportedJson,
                        onGenerateExport = { viewModel.generateDatabaseExport() },
                        onChangeLanguageClick = { viewModel.navigateTo(Screen.LANGUAGE_SELECTION) },
                        onBack = { viewModel.navigateBack() },
                        onSignOut = { viewModel.onAuthStateChanged(null) }
                    )
                }

                Screen.WORKOUT_DETAIL -> {
                    WorkoutDetailScreen(
                        workout = selectedWorkout,
                        exercises = selectedExercises,
                        isBangla = isBangla,
                        onBack = { viewModel.navigateBack() },
                        onStartWorkout = { workout ->
                            viewModel.startWorkoutSession(workout)
                        },
                        onExerciseClick = { ex ->
                            viewModel.openVideoPlayer(ex)
                        }
                    )
                }

                Screen.EXERCISE_VIDEO_PLAYER -> {
                    ExerciseVideoPlayerScreen(
                        exercise = videoExercise,
                        isBangla = isBangla,
                        onNextExercise = {
                            val list = selectedExercises
                            val currIdx = list.indexOfFirst { it.id == videoExercise?.id }
                            if (currIdx >= 0 && currIdx + 1 < list.size) {
                                viewModel.openVideoPlayer(list[currIdx + 1])
                            } else {
                                viewModel.navigateBack()
                            }
                        },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                Screen.ACTIVE_WORKOUT -> {
                    val currentEx = selectedExercises.getOrNull(currentExIndex)
                    val nextEx = selectedExercises.getOrNull(currentExIndex + 1)
                    ActiveWorkoutScreen(
                        currentExercise = currentEx,
                        nextExercise = nextEx,
                        currentIndex = currentExIndex,
                        totalExercises = selectedExercises.size,
                        timerSeconds = timerSec,
                        totalSeconds = totalSec,
                        isPaused = isPaused,
                        isBangla = isBangla,
                        onPauseToggle = { viewModel.togglePauseTimer() },
                        onSkip = { viewModel.skipCurrentExercise() },
                        onFinish = { viewModel.finishWorkoutSession() },
                        onBack = { viewModel.navigateBack() },
                        onOpenVideo = { currentEx?.let { viewModel.openVideoPlayer(it) } }
                    )
                }

                Screen.REST_TIMER -> {
                    val nextEx = selectedExercises.getOrNull(currentExIndex + 1)
                    RestTimerScreen(
                        restSecondsRemaining = restSec,
                        totalRestSeconds = 15,
                        nextExercise = nextEx,
                        isBangla = isBangla,
                        onSkipRest = { viewModel.skipRest() }
                    )
                }

                Screen.WORKOUT_COMPLETION -> {
                    WorkoutCompletionScreen(
                        durationMinutes = completedDuration,
                        exercisesCompleted = completedCount,
                        caloriesBurned = completedCalories,
                        isBangla = isBangla,
                        onViewProgress = {
                            viewModel.navigateTo(Screen.PROGRESS, addToBackStack = false)
                        },
                        onBackHome = {
                            viewModel.navigateTo(Screen.HOME, addToBackStack = false)
                        }
                    )
                }

                Screen.CALENDAR -> {
                    CalendarScreen(
                        isBangla = isBangla,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                Screen.HISTORY -> {
                    WorkoutHistoryScreen(
                        historyList = workoutHistory,
                        isBangla = isBangla,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                Screen.DOWNLOADS -> {
                    DownloadsScreen(
                        downloadedExercises = downloadedExercises,
                        isBangla = isBangla,
                        onDeleteDownload = { exId ->
                            viewModel.toggleExerciseDownload(exId, true)
                        },
                        onBack = { viewModel.navigateBack() },
                        onExerciseClick = { ex -> viewModel.openVideoPlayer(ex) }
                    )
                }

                Screen.FAVORITES -> {
                    FavoritesScreen(
                        favoriteExercises = favoriteExercises,
                        isBangla = isBangla,
                        onToggleFavorite = { id, currentFav ->
                            viewModel.toggleExerciseFavorite(id, currentFav)
                        },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                Screen.REMINDERS -> {
                    ReminderScreen(
                        initialEnabled = reminderSettings?.isEnabled ?: true,
                        initialTime = reminderSettings?.reminderTime ?: "07:00 AM",
                        initialDays = reminderSettings?.selectedDays ?: "Mon,Tue,Wed,Thu,Fri,Sat,Sun",
                        isBangla = isBangla,
                        onSaveReminder = { enabled, time, days ->
                            viewModel.saveReminder(enabled, time, days)
                        },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                else -> {
                    HomeScreen(
                        userProfile = userProfile,
                        workouts = allWorkouts,
                        isBangla = isBangla,
                        themeMode = themeMode,
                        onWorkoutClick = { workout ->
                            viewModel.selectWorkout(workout)
                            viewModel.navigateTo(Screen.WORKOUT_DETAIL)
                        },
                        onStartWorkout = { workout ->
                            viewModel.startWorkoutSession(workout)
                        },
                        onNotificationClick = {
                            viewModel.navigateTo(Screen.REMINDERS)
                        },
                        onCalendarClick = {
                            viewModel.navigateTo(Screen.CALENDAR)
                        },
                        onToggleLanguage = {
                            viewModel.toggleLanguage()
                        },
                        onToggleTheme = {
                            viewModel.toggleThemeMode()
                        }
                    )
                }
            }
        }
    }
}
