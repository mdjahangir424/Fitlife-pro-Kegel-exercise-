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
        root.put("category", "Pelvic Floor & Kegel Exercises")
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

    suspend fun exportAllDataAsJson(): String = exportDatabaseToJson()

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val existingProfile = database.userProfileDao().getUserProfileOnce()
        if (existingProfile == null) {
            database.userProfileDao().insertOrUpdate(
                UserProfileEntity(
                    id = 1,
                    name = "জাহাঙ্গীর আলম",
                    ageRange = "25 - 35",
                    experienceLevel = "Beginner",
                    goal = "Pelvic Floor Strength",
                    language = "bn",
                    hasCompletedOnboarding = true
                )
            )
        }

        // Single Category: Pelvic Kegel Exercise
        val kegelWorkouts = listOf(
            WorkoutEntity(
                id = "wb_kegel_main",
                titleBn = "১০ ক্যাগেল এক্সারসাইজ রুটিন",
                titleEn = "10 Pelvic Kegel Exercises Routine",
                category = "pelvic_kegel",
                durationMinutes = 20,
                exerciseCount = 10,
                levelBn = "সব স্তর (All Levels)",
                levelEn = "All Levels",
                descriptionBn = "পেলভিক ফ্লোর পেশি মজবুত, ব্লাডার নিয়ন্ত্রণ এবং কোর সক্ষমতা বৃদ্ধির জন্য ১০টি বৈজ্ঞানিক ক্যাগেল আসন ও ব্যায়াম।",
                descriptionEn = "10 medically proven pelvic floor & Kegel exercises designed to strengthen pelvic muscles, core stability, and control.",
                colorHex = "#00B4D8"
            ),
            WorkoutEntity(
                id = "wb_kegel_quick",
                titleBn = "দ্রুত পেলভিক শক্তি ও ফ্লিক",
                titleEn = "Quick Pelvic Floor Power",
                category = "pelvic_kegel",
                durationMinutes = 12,
                exerciseCount = 5,
                levelBn = "শুরু করছি (Beginner)",
                levelEn = "Beginner",
                descriptionBn = "কম সময়ে পেলভিক পেশির দ্রুত সংকোচন ও শক্তি বৃদ্ধির কার্যকরী সেশন।",
                descriptionEn = "A fast and effective session focusing on rapid contraction and pelvic muscle activation.",
                colorHex = "#38BDF8"
            ),
            WorkoutEntity(
                id = "wb_kegel_release",
                titleBn = "পেলভিক রিল্যাক্সেশন ও স্ট্রেচ",
                titleEn = "Pelvic Release & Flexibility",
                category = "pelvic_kegel",
                durationMinutes = 15,
                exerciseCount = 5,
                levelBn = "নরম ও রিল্যাক্স",
                levelEn = "Relax & Restore",
                descriptionBn = "পেলভিক পেশির টান দূর করা এবং গভীর শ্বাসের সাথে শিথিলতা আনয়নের বিশেষ আসন।",
                descriptionEn = "Gentle stretching and diaphragmatic breathing routines to release tension and improve elasticity.",
                colorHex = "#0284C7"
            )
        )
        database.workoutDao().insertAll(kegelWorkouts)

        // 10 Distinct, Medically Proven Pelvic Kegel Exercises with 3D animation keys
        val tenKegelExercises = listOf(
            ExerciseEntity(
                id = "ex_kegel_basic",
                workoutId = "wb_kegel_main",
                titleBn = "১. মৌলিক ক্যাগেল সংকোচন ও হোল্ড",
                titleEn = "1. Basic Kegel Contract & Hold",
                targetBn = "১০ বার (প্রতিটি ৫-১০ সেকেন্ড)",
                targetEn = "10 reps (5-10s hold)",
                durationSec = 45,
                restSec = 20,
                animationType = "kegel_basic",
                instructionsBn = "চিত হয়ে শুয়ে হাঁটু ভাঁজ করুন। পেলভিক ফ্লোর পেশি ওপরের দিকে টেনে শক্ত করে ৫-১০ সেকেন্ড ধরে রাখুন, তারপর ধীরে ধীরে শ্বাস ছেড়ে শিথিল করুন।",
                instructionsEn = "Lie on back with knees bent. Squeeze and lift pelvic floor muscles, hold for 5-10 seconds, then slowly release and relax.",
                isFavorite = true,
                isDownloaded = true,
                downloadSizeMb = 8.5
            ),
            ExerciseEntity(
                id = "ex_kegel_quick",
                workoutId = "wb_kegel_main",
                titleBn = "২. কুইক ফ্লিক ক্যাগেল (দ্রুত সংকোচন)",
                titleEn = "2. Quick Flick Kegels",
                targetBn = "১৫ বার দ্রুত ফ্লিক",
                targetEn = "15 quick pulses",
                durationSec = 35,
                restSec = 15,
                animationType = "kegel_quick_flicks",
                instructionsBn = "পেলভিক পেশিকে দ্রুত ১ সেকেন্ডের ব্যবধানে সংকুচিত ও অবিলম্বে মুক্ত করুন। এটি পেশির দ্রুত প্রতিক্রিয়া তৈরি করে।",
                instructionsEn = "Rapidly contract and immediately release pelvic muscles in 1-second pulses to build fast-twitch reflexes.",
                isFavorite = true,
                isDownloaded = true,
                downloadSizeMb = 9.2
            ),
            ExerciseEntity(
                id = "ex_kegel_bridge",
                workoutId = "wb_kegel_main",
                titleBn = "৩. গ্লুট ব্রিজ ক্যাগেল লিফট",
                titleEn = "3. Glute Bridge Kegel",
                targetBn = "১২ বার",
                targetEn = "12 reps",
                durationSec = 45,
                restSec = 20,
                animationType = "kegel_glute_bridge",
                instructionsBn = "নিতম্ব ওপরের দিকে তুলে শরীরকে সেতু বা ব্রিজের মতো করুন। উপরে ওঠার সময় ক্যাগেল পেশি শক্ত করে চেপে ধরুন, নিচে নামার সময় শিথিল করুন।",
                instructionsEn = "Elevate hips into a bridge while tightly engaging glutes and pelvic floor muscles. Lower smoothly.",
                isFavorite = true,
                isDownloaded = true,
                downloadSizeMb = 10.4
            ),
            ExerciseEntity(
                id = "ex_kegel_butterfly",
                workoutId = "wb_kegel_main",
                titleBn = "৪. বাটারফ্লাই পেলভিক স্ট্রেচ",
                titleEn = "4. Reclined Butterfly Kegel",
                targetBn = "৪০ সেকেন্ড",
                targetEn = "40 seconds",
                durationSec = 40,
                restSec = 20,
                animationType = "kegel_butterfly",
                instructionsBn = "পায়ের তালু দুটি একসাথে স্পর্শ করে হাঁটু দুই পাশে প্রজাপতির ডানার মতো খুলে দিন। গভীর শ্বাস নিয়ে পেলভিক পেশির সম্প্রসারণ অনুভব করুন।",
                instructionsEn = "Bring soles of feet together allowing knees to fall wide. Take deep belly breaths and rhythmically activate pelvic floor.",
                isFavorite = true,
                isDownloaded = true,
                downloadSizeMb = 7.8
            ),
            ExerciseEntity(
                id = "ex_kegel_cat_cow",
                workoutId = "wb_kegel_main",
                titleBn = "৫. ক্যাট-কাউ পেলভিক মুভমেন্ট",
                titleEn = "5. Cat-Cow Pelvic Movement",
                targetBn = "১০ চক্র",
                targetEn = "10 cycles",
                durationSec = 45,
                restSec = 15,
                animationType = "kegel_cat_cow",
                instructionsBn = "চার পায়ে টেবিলের মতো অবস্থান নিন। পিঠ বাঁকানোর সময় পেলভিক পেশি সংকোচন করুন এবং পেট নামানোর সময় শিথিল করে শ্বাস নিন।",
                instructionsEn = "On hands and knees, arch spine on exhale with pelvic contraction, then dip spine on inhale with pelvic relaxation.",
                isFavorite = true,
                isDownloaded = true,
                downloadSizeMb = 8.9
            ),
            ExerciseEntity(
                id = "ex_kegel_bird_dog",
                workoutId = "wb_kegel_main",
                titleBn = "৬. বার্ড-ডগ পেলভিক ব্রেইস",
                titleEn = "6. Bird Dog Pelvic Brace",
                targetBn = "১০ বার (প্রতি পাশ)",
                targetEn = "10 reps/side",
                durationSec = 45,
                restSec = 20,
                animationType = "kegel_bird_dog",
                instructionsBn = "বিপরীত হাত ও পা সোজা প্রসারিত করুন। কোর ও পেলভিক পেশি শক্ত রেখে শরীরের ভারসাম্য বজায় রাখুন।",
                instructionsEn = "Extend opposite arm and leg straight out while bracing core and stabilizing pelvic floor.",
                isFavorite = false,
                isDownloaded = true,
                downloadSizeMb = 11.1
            ),
            ExerciseEntity(
                id = "ex_kegel_squat",
                workoutId = "wb_kegel_main",
                titleBn = "৭. ডিপ পেলভিক স্কোয়াট (মালাসন)",
                titleEn = "7. Deep Pelvic Squat (Malasana)",
                targetBn = "১২ বার",
                targetEn = "12 reps",
                durationSec = 45,
                restSec = 25,
                animationType = "kegel_deep_squat",
                instructionsBn = "পা চওড়া করে নিচে গভীরভাবে বসুন। নিচে নামার সময় পেলভিক ফ্লোর প্রসারিত করুন এবং ওঠার সময় টেনে সংকুচিত করুন।",
                instructionsEn = "Perform deep wide stance squat. Open pelvic diaphragm at the bottom and lift pelvic floor on the way up.",
                isFavorite = true,
                isDownloaded = true,
                downloadSizeMb = 9.8
            ),
            ExerciseEntity(
                id = "ex_kegel_clamshell",
                workoutId = "wb_kegel_main",
                titleBn = "৮. ক্ল্যামশেল পেলভিক স্টেবিলিটি",
                titleEn = "8. Clamshell Pelvic Stability",
                targetBn = "১২ বার (প্রতি পাশ)",
                targetEn = "12 reps/side",
                durationSec = 40,
                restSec = 20,
                animationType = "kegel_clamshell",
                instructionsBn = "কাত হয়ে শুয়ে হাঁটু ৪৫ ডিগ্রিতে রাখুন। পায়ের পাতা সংযুক্ত রেখে ওপরের হাঁটু ঝিনুকের মতো খুলুন ও পেলভিক অংশ শক্ত রাখুন।",
                instructionsEn = "Lie on your side with knees at 45 degrees. Open top knee like a clamshell while holding pelvic stability.",
                isFavorite = false,
                isDownloaded = true,
                downloadSizeMb = 8.1
            ),
            ExerciseEntity(
                id = "ex_kegel_dead_bug",
                workoutId = "wb_kegel_main",
                titleBn = "৯. ডেড বাগ কোর ও পেলভিক ব্রেইস",
                titleEn = "9. Dead Bug Pelvic Core",
                targetBn = "১০ বার (প্রতি পাশ)",
                targetEn = "10 reps/side",
                durationSec = 45,
                restSec = 20,
                animationType = "kegel_dead_bug",
                instructionsBn = "পিঠ মেঝের সাথে সম্পূর্ণ সোজা রাখুন। বিপরীত হাত ও পা নামানোর সময় পেলভিক ফ্লোর সংকুচিত করে কোর ধরে রাখুন।",
                instructionsEn = "Lie on back with arms and knees up. Slowly lower opposite arm and leg while engaging deep pelvic core.",
                isFavorite = false,
                isDownloaded = true,
                downloadSizeMb = 10.2
            ),
            ExerciseEntity(
                id = "ex_kegel_happy_baby",
                workoutId = "wb_kegel_main",
                titleBn = "১০. হ্যাপি বেবি পেলভিক রিলিজ",
                titleEn = "10. Happy Baby Pelvic Release",
                targetBn = "৪৫ সেকেন্ড রিল্যাক্স",
                targetEn = "45s relax",
                durationSec = 45,
                restSec = 15,
                animationType = "kegel_happy_baby",
                instructionsBn = "হাঁটু বুকের কাছে টেনে পায়ের পাতার বাইরের অংশ হাত দিয়ে ধরুন। পেলভিক ফ্লোর পেশি সম্পূর্ণ শিথিল করে শান্তভাবে শ্বাস নিন।",
                instructionsEn = "Lie on back holding feet gently pulling knees towards armpits. Completely relax the pelvic floor and decompress.",
                isFavorite = true,
                isDownloaded = true,
                downloadSizeMb = 7.4
            )
        )
        database.exerciseDao().insertAll(tenKegelExercises)

        // Seed initial history
        val now = System.currentTimeMillis()
        val dayMs = 86400000L
        val defaultHistory = listOf(
            WorkoutHistoryEntity(
                workoutId = "wb_kegel_main",
                workoutTitleBn = "১০ ক্যাগেল এক্সারসাইজ রুটিন",
                workoutTitleEn = "10 Pelvic Kegel Exercises Routine",
                timestamp = now - (dayMs * 0),
                durationMinutes = 20,
                exercisesCompleted = 10,
                totalExercises = 10,
                caloriesBurned = 110
            ),
            WorkoutHistoryEntity(
                workoutId = "wb_kegel_quick",
                workoutTitleBn = "দ্রুত পেলভিক শক্তি ও ফ্লিক",
                workoutTitleEn = "Quick Pelvic Floor Power",
                timestamp = now - (dayMs * 1),
                durationMinutes = 12,
                exercisesCompleted = 5,
                totalExercises = 5,
                caloriesBurned = 75
            ),
            WorkoutHistoryEntity(
                workoutId = "wb_kegel_main",
                workoutTitleBn = "১০ ক্যাগেল এক্সারসাইজ রুটিন",
                workoutTitleEn = "10 Pelvic Kegel Exercises Routine",
                timestamp = now - (dayMs * 2),
                durationMinutes = 20,
                exercisesCompleted = 10,
                totalExercises = 10,
                caloriesBurned = 110
            ),
            WorkoutHistoryEntity(
                workoutId = "wb_kegel_release",
                workoutTitleBn = "পেলভিক রিল্যাক্সেশন ও স্ট্রেচ",
                workoutTitleEn = "Pelvic Release & Flexibility",
                timestamp = now - (dayMs * 3),
                durationMinutes = 15,
                exercisesCompleted = 5,
                totalExercises = 5,
                caloriesBurned = 60
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
