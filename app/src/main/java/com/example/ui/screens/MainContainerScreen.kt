package com.example.ui.screens

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
import androidx.compose.material3.Icon
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
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.util.LanguageStrings
import com.example.viewmodel.FitLifeViewModel
import com.example.viewmodel.Screen

@Composable
fun MainContainerScreen(viewModel: FitLifeViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val isBangla = (userProfile?.language ?: "bn") == "bn"

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

    val showBottomBar = currentScreen in listOf(Screen.HOME, Screen.CATEGORIES, Screen.PROGRESS, Screen.PROFILE)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = SurfaceCard,
                    contentColor = TextPrimary,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                ) {
                    NavigationBarItem(
                        selected = currentScreen == Screen.HOME,
                        onClick = { viewModel.navigateTo(Screen.HOME, addToBackStack = false) },
                        icon = {
                            Icon(imageVector = Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(24.dp))
                        },
                        label = {
                            Text(
                                text = LanguageStrings.get("nav_home", isBangla),
                                fontSize = 11.sp,
                                fontWeight = if (currentScreen == Screen.HOME) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonEmerald,
                            indicatorColor = NeonEmerald,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.CATEGORIES,
                        onClick = { viewModel.navigateTo(Screen.CATEGORIES, addToBackStack = false) },
                        icon = {
                            Icon(imageVector = Icons.Default.Category, contentDescription = "Categories", modifier = Modifier.size(24.dp))
                        },
                        label = {
                            Text(
                                text = LanguageStrings.get("nav_categories", isBangla),
                                fontSize = 11.sp,
                                fontWeight = if (currentScreen == Screen.CATEGORIES) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonEmerald,
                            indicatorColor = NeonEmerald,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_categories")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.PROGRESS,
                        onClick = { viewModel.navigateTo(Screen.PROGRESS, addToBackStack = false) },
                        icon = {
                            Icon(imageVector = Icons.Default.BarChart, contentDescription = "Progress", modifier = Modifier.size(24.dp))
                        },
                        label = {
                            Text(
                                text = LanguageStrings.get("nav_progress", isBangla),
                                fontSize = 11.sp,
                                fontWeight = if (currentScreen == Screen.PROGRESS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonEmerald,
                            indicatorColor = NeonEmerald,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_progress")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.PROFILE,
                        onClick = { viewModel.navigateTo(Screen.PROFILE, addToBackStack = false) },
                        icon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(24.dp))
                        },
                        label = {
                            Text(
                                text = LanguageStrings.get("nav_profile", isBangla),
                                fontSize = 11.sp,
                                fontWeight = if (currentScreen == Screen.PROFILE) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonEmerald,
                            indicatorColor = NeonEmerald,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_profile")
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
                        onLanguageSelected = { lang -> viewModel.setLanguage(lang) },
                        onContinue = { viewModel.navigateTo(Screen.PROFILE_SETUP) }
                    )
                }

                Screen.PROFILE_SETUP -> {
                    ProfileSetupScreen(
                        initialName = userProfile?.name ?: "",
                        initialAge = userProfile?.ageRange ?: "25 - 35",
                        initialExp = userProfile?.experienceLevel ?: "Beginner",
                        initialGoal = userProfile?.goal ?: "General Fitness",
                        isBangla = isBangla,
                        onSave = { name, age, exp, goal ->
                            viewModel.updateProfile(name, age, exp, goal, userProfile?.language ?: "bn")
                            viewModel.navigateTo(Screen.HOME, addToBackStack = false)
                        }
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

                Screen.PROFILE, Screen.ABOUT_OWNER -> {
                    AboutOwnerScreen(
                        isBangla = isBangla,
                        exportedJson = exportedJson,
                        onGenerateExport = { viewModel.generateDatabaseExport() },
                        onChangeLanguageClick = { viewModel.navigateTo(Screen.LANGUAGE_SELECTION) },
                        onBack = { viewModel.navigateBack() }
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
