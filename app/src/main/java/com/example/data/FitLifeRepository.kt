package com.example.data

import android.content.Context
import com.example.data.model.ExerciseEntity
import com.example.data.model.ReminderSettingsEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutHistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FitLifeRepository(private val database: FitLifeDatabase) {

    val userProfile: Flow<UserProfileEntity?> = database.userProfileDao().getUserProfile()
    val allWorkouts: Flow<List<WorkoutEntity>> = database.workoutDao().getAllWorkouts()
    val allExercises: Flow<List<ExerciseEntity>> = database.exerciseDao().getAllExercises()
    val favoriteExercises: Flow<List<ExerciseEntity>> = database.exerciseDao().getFavoriteExercises()
    val downloadedExercises: Flow<List<ExerciseEntity>> = database.exerciseDao().getDownloadedExercises()
    val workoutHistory: Flow<List<WorkoutHistoryEntity>> = database.workoutHistoryDao().getAllHistory()
    val reminderSettings: Flow<ReminderSettingsEntity?> = database.reminderSettingsDao().getReminderSettings()
    val totalWorkoutsCount: Flow<Int> = database.workoutHistoryDao().getTotalWorkoutsCount()
    val totalMinutes: Flow<Int?> = database.workoutHistoryDao().getTotalMinutes()

    suspend fun saveProfile(profile: UserProfileEntity) {
        database.userProfileDao().insertOrUpdate(profile)
    }

    suspend fun getWorkoutById(id: String): WorkoutEntity? {
        return database.workoutDao().getWorkoutById(id)
    }

    fun getExercisesForWorkout(workoutId: String): Flow<List<ExerciseEntity>> {
        return database.exerciseDao().getExercisesForWorkout(workoutId)
    }

    suspend fun getExercisesForWorkoutOnce(workoutId: String): List<ExerciseEntity> {
        return database.exerciseDao().getExercisesForWorkoutOnce(workoutId)
    }

    suspend fun toggleFavoriteExercise(exerciseId: String, isFav: Boolean) {
        database.exerciseDao().toggleFavorite(exerciseId, isFav)
    }

    suspend fun toggleDownloadExercise(exerciseId: String, isDownloaded: Boolean) {
        database.exerciseDao().setDownloaded(exerciseId, isDownloaded)
    }

    suspend fun logCompletedWorkout(
        workoutId: String,
        titleBn: String,
        titleEn: String,
        durationMinutes: Int,
        exercisesCompleted: Int,
        totalExercises: Int,
        calories: Int
    ) {
        val history = WorkoutHistoryEntity(
            workoutId = workoutId,
            workoutTitleBn = titleBn,
            workoutTitleEn = titleEn,
            timestamp = System.currentTimeMillis(),
            durationMinutes = durationMinutes,
            exercisesCompleted = exercisesCompleted,
            totalExercises = totalExercises,
            caloriesBurned = calories
        )
        database.workoutHistoryDao().insertHistory(history)
    }

    suspend fun saveReminder(settings: ReminderSettingsEntity) {
        database.reminderSettingsDao().saveReminderSettings(settings)
    }

    suspend fun exportDatabaseToJson(): String = withContext(Dispatchers.IO) {
        val root = JSONObject()
        root.put("appName", "FitLife Pro")
        root.put("owner", "Md Jahangir Alam")
        root.put("ownerEmail", "mdjahangiralam2528@gmail.com")
        root.put("exportTimestamp", System.currentTimeMillis())
        root.put("date", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))

        // Profile
        val profile = database.userProfileDao().getUserProfileOnce()
        if (profile != null) {
            val profJson = JSONObject()
            profJson.put("name", profile.name)
            profJson.put("ageRange", profile.ageRange)
            profJson.put("experienceLevel", profile.experienceLevel)
            profJson.put("goal", profile.goal)
            profJson.put("language", profile.language)
            root.put("profile", profJson)
        }

        // History
        val historyList = database.workoutHistoryDao().getRecentHistory()
        val historyArray = JSONArray()
        for (item in historyList) {
            val h = JSONObject()
            h.put("workoutTitle", item.workoutTitleEn)
            h.put("workoutTitleBn", item.workoutTitleBn)
            h.put("durationMinutes", item.durationMinutes)
            h.put("exercisesCompleted", item.exercisesCompleted)
            h.put("caloriesBurned", item.caloriesBurned)
            h.put("timestamp", item.timestamp)
            historyArray.put(h)
        }
        root.put("history", historyArray)

        return@withContext root.toString(2)
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val existingProfile = database.userProfileDao().getUserProfileOnce()
        if (existingProfile == null) {
            database.userProfileDao().insertOrUpdate(
                UserProfileEntity(
                    id = 1,
                    name = "জাহাঙ্গীর আলম",
                    ageRange = "25 - 35",
                    experienceLevel = "Beginner",
                    goal = "General Fitness",
                    language = "bn",
                    hasCompletedOnboarding = true
                )
            )
        }

        val workouts = database.workoutDao().getWorkoutById("wb_full_body")
        if (workouts == null) {
            val defaultWorkouts = listOf(
                WorkoutEntity(
                    id = "wb_full_body",
                    titleBn = "সম্পূর্ণ শরীর ওয়ার্কআউট",
                    titleEn = "Full Body Workout",
                    category = "full_body",
                    durationMinutes = 30,
                    exerciseCount = 12,
                    levelBn = "শুরু করছি (Beginner)",
                    levelEn = "Beginner",
                    descriptionBn = "সারা শরীরের জন্য একটি সম্পূর্ণ ওয়ার্কআউট প্রোগ্রাম। শক্তি, সহনশীলতা ও ফিটনেস বাড়ান।",
                    descriptionEn = "A complete full-body routine designed to build strength, endurance, and overall conditioning.",
                    colorHex = "#FF6D00"
                ),
                WorkoutEntity(
                    id = "wb_abs",
                    titleBn = "পেট ও কোর ওয়ার্কআউট",
                    titleEn = "Abs & Core",
                    category = "abs",
                    durationMinutes = 20,
                    exerciseCount = 10,
                    levelBn = "মধ্যবর্তী",
                    levelEn = "Intermediate",
                    descriptionBn = "পেটের পেশি মজবুত করতে এবং কোর স্টেবিলিটি বাড়াতে কার্যকরী আসন ও ব্যায়াম।",
                    descriptionEn = "Sculpt and strengthen your midsection with targeted core stability movements.",
                    colorHex = "#00B0FF"
                ),
                WorkoutEntity(
                    id = "wb_chest",
                    titleBn = "বুকের ওয়ার্কআউট",
                    titleEn = "Chest Workout",
                    category = "chest",
                    durationMinutes = 25,
                    exerciseCount = 8,
                    levelBn = "শুরু করছি",
                    levelEn = "Beginner",
                    descriptionBn = "বুকের শক্তি ও আকার বৃদ্ধির জন্য পুশ-আপের বিভিন্ন বৈচিত্র্য।",
                    descriptionEn = "Target upper and lower pectoral muscles with progressive push variations.",
                    colorHex = "#FF5252"
                ),
                WorkoutEntity(
                    id = "wb_arms",
                    titleBn = "বাহুর ওয়ার্কআউট",
                    titleEn = "Arms Workout",
                    category = "arms",
                    durationMinutes = 20,
                    exerciseCount = 8,
                    levelBn = "সব স্তর",
                    levelEn = "All Levels",
                    descriptionBn = "বাইসেপ ও ট্রাইসেপের শক্তি ও টোন বাড়ানোর ব্যায়াম।",
                    descriptionEn = "Strengthen and tone your biceps, triceps, and forearms with bodyweight mastery.",
                    colorHex = "#00C853"
                ),
                WorkoutEntity(
                    id = "wb_shoulders",
                    titleBn = "কাঁধের ওয়ার্কআউট",
                    titleEn = "Shoulders Workout",
                    category = "shoulders",
                    durationMinutes = 25,
                    exerciseCount = 8,
                    levelBn = "মধ্যবর্তী",
                    levelEn = "Intermediate",
                    descriptionBn = "প্রশস্ত ও শক্তিশালী কাঁধ গঠনের জন্য ডেল্টয়েড ব্যায়াম।",
                    descriptionEn = "Improve shoulder stability and develop sculpted deltoids with focused sets.",
                    colorHex = "#0091EA"
                ),
                WorkoutEntity(
                    id = "wb_back",
                    titleBn = "পিঠের ওয়ার্কআউট",
                    titleEn = "Back Workout",
                    category = "back",
                    durationMinutes = 25,
                    exerciseCount = 8,
                    levelBn = "শুরু করছি",
                    levelEn = "Beginner",
                    descriptionBn = "পিঠের ব্যথা দূর ও মেরুদণ্ড সোজা রাখার জন্য ল্যাট ও ট্র্যাপ ব্যায়াম।",
                    descriptionEn = "Strengthen posture and back muscles to relieve tension and build upper-body power.",
                    colorHex = "#304FFE"
                ),
                WorkoutEntity(
                    id = "wb_legs",
                    titleBn = "পায়ের ওয়ার্কআউট",
                    titleEn = "Legs Workout",
                    category = "legs",
                    durationMinutes = 30,
                    exerciseCount = 10,
                    levelBn = "শুরু করছি",
                    levelEn = "Beginner",
                    descriptionBn = "কোয়াড, হ্যামস্ট্রিং এবং কাফ পেশির শক্তির জন্য কার্যকরী স্কোয়াট ও লাঞ্জেস।",
                    descriptionEn = "Build explosive lower-body power and balance with functional squats and lunges.",
                    colorHex = "#7C4DFF"
                ),
                WorkoutEntity(
                    id = "wb_stretching",
                    titleBn = "স্ট্রেচিং ও রিল্যাক্স",
                    titleEn = "Stretching & Cool Down",
                    category = "stretching",
                    durationMinutes = 15,
                    exerciseCount = 6,
                    levelBn = "সব স্তর",
                    levelEn = "All Levels",
                    descriptionBn = "ব্যায়ামের পর শরীরকে শিথিল ও নমনীয় করার জন্য স্ট্রেচিং।",
                    descriptionEn = "Improve flexibility, release lactic acid, and prevent stiffness with relaxing stretches.",
                    colorHex = "#00B4D8"
                )
            )
            database.workoutDao().insertAll(defaultWorkouts)

            // Seed exercises for full body
            val fullBodyExercises = listOf(
                ExerciseEntity(
                    id = "ex_pushup",
                    workoutId = "wb_full_body",
                    titleBn = "Push Up (বুক ডাউন)",
                    titleEn = "Push Up",
                    targetBn = "১২ বার",
                    targetEn = "12 reps",
                    durationSec = 45,
                    restSec = 30,
                    animationType = "push_up",
                    instructionsBn = "হাত কাঁধের সমান্তরালে রাখুন, শরীর সোজা রেখে বুক মেঝে পর্যন্ত নামান ও উপরে উঠুন।",
                    instructionsEn = "Keep your body in a straight plank line. Lower chest to floor and push back up firmly.",
                    isFavorite = true,
                    isDownloaded = true,
                    downloadSizeMb = 12.5
                ),
                ExerciseEntity(
                    id = "ex_squat",
                    workoutId = "wb_full_body",
                    titleBn = "Squat (স্কোয়াট)",
                    titleEn = "Squat",
                    targetBn = "১৫ বার",
                    targetEn = "15 reps",
                    durationSec = 45,
                    restSec = 25,
                    animationType = "squat",
                    instructionsBn = "পা দুটি কাঁধ বরাবর ফাঁক করে নিতম্ব পেছনে নিয়ে বসুন, হাঁটু যাতে পায়ের আঙুল না ছাড়ায়।",
                    instructionsEn = "Stand shoulder-width apart. Push hips back and lower until thighs are parallel to ground.",
                    isFavorite = true,
                    isDownloaded = true,
                    downloadSizeMb = 15.2
                ),
                ExerciseEntity(
                    id = "ex_plank",
                    workoutId = "wb_full_body",
                    titleBn = "Plank (প্ল্যাঙ্ক)",
                    titleEn = "Plank",
                    targetBn = "৪০ সেকেন্ড",
                    targetEn = "40 seconds",
                    durationSec = 40,
                    restSec = 30,
                    animationType = "plank",
                    instructionsBn = "কনুই ও পায়ের আঙুলের ওপর ভর দিয়ে শরীর সোজা রেখে কোর শক্ত করে ধরে রাখুন।",
                    instructionsEn = "Rest on forearms and toes. Keep your spine neutral and engage your core tightly.",
                    isFavorite = true,
                    isDownloaded = true,
                    downloadSizeMb = 8.7
                ),
                ExerciseEntity(
                    id = "ex_mountain",
                    workoutId = "wb_full_body",
                    titleBn = "Mountain Climber (মাউন্টেন ক্লাইম্বার)",
                    titleEn = "Mountain Climber",
                    targetBn = "৩০ সেকেন্ড",
                    targetEn = "30 seconds",
                    durationSec = 30,
                    restSec = 25,
                    animationType = "mountain_climber",
                    instructionsBn = "পুশ-আপ অবস্থানে থেকে পর্যায়ক্রমে হাঁটু বুকের কাছে দ্রুত টেনে আনুন।",
                    instructionsEn = "From high plank, rapidly drive knees forward toward chest in alternating running rhythm.",
                    isFavorite = true,
                    isDownloaded = true,
                    downloadSizeMb = 14.1
                ),
                ExerciseEntity(
                    id = "ex_lunges",
                    workoutId = "wb_full_body",
                    titleBn = "Lunge (লাঞ্জেস)",
                    titleEn = "Forward Lunges",
                    targetBn = "১২ বার (প্রতি পা)",
                    targetEn = "12 reps/leg",
                    durationSec = 45,
                    restSec = 25,
                    animationType = "lunges",
                    instructionsBn = "এক পা সামনে বাড়িয়ে হাঁটু ৯০ ডিগ্রি কোণে বাঁকিয়ে বসুন, তারপর আগের অবস্থানে ফিরে আসুন।",
                    instructionsEn = "Step forward with one leg, lowering hips until both knees bend at a 90-degree angle.",
                    isFavorite = false,
                    isDownloaded = true,
                    downloadSizeMb = 12.1
                ),
                ExerciseEntity(
                    id = "ex_jumping",
                    workoutId = "wb_full_body",
                    titleBn = "Jumping Jacks (জাম্পিং জ্যাক)",
                    titleEn = "Jumping Jacks",
                    targetBn = "৪৫ সেকেন্ড",
                    targetEn = "45 seconds",
                    durationSec = 45,
                    restSec = 20,
                    animationType = "jumping_jacks",
                    instructionsBn = "লাফিয়ে পা ফাঁক করে হাত মাথার উপর তুলুন এবং পুনরায় স্বাভাবিক অবস্থানে ফিরে আসুন।",
                    instructionsEn = "Jump feet outward while raising arms overhead, then return to starting position briskly.",
                    isFavorite = false,
                    isDownloaded = true,
                    downloadSizeMb = 9.8
                ),
                ExerciseEntity(
                    id = "ex_crunches",
                    workoutId = "wb_full_body",
                    titleBn = "Abdominal Crunches (ক্রাঞ্চেস)",
                    titleEn = "Abdominal Crunches",
                    targetBn = "১৫ বার",
                    targetEn = "15 reps",
                    durationSec = 40,
                    restSec = 25,
                    animationType = "crunches",
                    instructionsBn = "চিৎ হয়ে শুয়ে হাঁটু ভাঁজ করুন। পেটের শক্তিতে কাঁধ মেঝে থেকে সামান্য উপরে তুলুন।",
                    instructionsEn = "Lie on back with knees bent. Contract abdominals and lift shoulder blades off mat.",
                    isFavorite = false,
                    isDownloaded = true,
                    downloadSizeMb = 11.0
                ),
                ExerciseEntity(
                    id = "ex_stretching",
                    workoutId = "wb_full_body",
                    titleBn = "Cobra Stretch (কোবরা স্ট্রেচ)",
                    titleEn = "Cobra Stretch",
                    targetBn = "৩০ সেকেন্ড",
                    targetEn = "30 seconds",
                    durationSec = 30,
                    restSec = 20,
                    animationType = "stretching",
                    instructionsBn = "উপুড় হয়ে শুয়ে হাতের তালুতে ভর দিয়ে বুক উপরের দিকে টানটান করুন।",
                    instructionsEn = "Lie prone and press hands down to gently arch chest upwards, opening spine and chest.",
                    isFavorite = true,
                    isDownloaded = true,
                    downloadSizeMb = 7.5
                )
            )
            database.exerciseDao().insertAll(fullBodyExercises)

            // Seed sample workout history matching UI mockup
            val now = System.currentTimeMillis()
            val dayMs = 86400000L
            val defaultHistory = listOf(
                WorkoutHistoryEntity(
                    workoutId = "wb_full_body",
                    workoutTitleBn = "Full Body Workout",
                    workoutTitleEn = "Full Body Workout",
                    timestamp = now - (dayMs * 0),
                    durationMinutes = 32,
                    exercisesCompleted = 12,
                    totalExercises = 12,
                    caloriesBurned = 210
                ),
                WorkoutHistoryEntity(
                    workoutId = "wb_abs",
                    workoutTitleBn = "Abs & Core",
                    workoutTitleEn = "Abs & Core",
                    timestamp = now - (dayMs * 1),
                    durationMinutes = 20,
                    exercisesCompleted = 10,
                    totalExercises = 10,
                    caloriesBurned = 145
                ),
                WorkoutHistoryEntity(
                    workoutId = "wb_chest",
                    workoutTitleBn = "Chest Workout",
                    workoutTitleEn = "Chest Workout",
                    timestamp = now - (dayMs * 2),
                    durationMinutes = 28,
                    exercisesCompleted = 8,
                    totalExercises = 8,
                    caloriesBurned = 180
                ),
                WorkoutHistoryEntity(
                    workoutId = "wb_legs",
                    workoutTitleBn = "Legs Workout",
                    workoutTitleEn = "Legs Workout",
                    timestamp = now - (dayMs * 3),
                    durationMinutes = 30,
                    exercisesCompleted = 10,
                    totalExercises = 10,
                    caloriesBurned = 230
                ),
                WorkoutHistoryEntity(
                    workoutId = "wb_shoulders",
                    workoutTitleBn = "Shoulders Workout",
                    workoutTitleEn = "Shoulders Workout",
                    timestamp = now - (dayMs * 5),
                    durationMinutes = 25,
                    exercisesCompleted = 8,
                    totalExercises = 8,
                    caloriesBurned = 160
                )
            )
            for (h in defaultHistory) {
                database.workoutHistoryDao().insertHistory(h)
            }

            database.reminderSettingsDao().saveReminderSettings(
                ReminderSettingsEntity(
                    id = 1,
                    isEnabled = true,
                    reminderTime = "07:00 AM",
                    selectedDays = "Mon,Tue,Wed,Thu,Fri,Sat,Sun"
                )
            )
        }
    }
}
