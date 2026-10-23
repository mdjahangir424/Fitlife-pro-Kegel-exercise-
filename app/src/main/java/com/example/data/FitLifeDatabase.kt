package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.ExerciseDao
import com.example.data.dao.ReminderSettingsDao
import com.example.data.dao.UserProfileDao
import com.example.data.dao.WorkoutDao
import com.example.data.dao.WorkoutHistoryDao
import com.example.data.model.ExerciseEntity
import com.example.data.model.ReminderSettingsEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutHistoryEntity

@Database(
    entities = [
        UserProfileEntity::class,
        WorkoutEntity::class,
        ExerciseEntity::class,
        WorkoutHistoryEntity::class,
        ReminderSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitLifeDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
    abstract fun reminderSettingsDao(): ReminderSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: FitLifeDatabase? = null

        fun getDatabase(context: Context): FitLifeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitLifeDatabase::class.java,
                    "fitlife_pro_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
