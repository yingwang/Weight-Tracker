package com.weighttracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weighttracker.data.dao.GoalDao
import com.weighttracker.data.dao.UserProfileDao
import com.weighttracker.data.dao.WeightEntryDao
import com.weighttracker.data.entity.Goal
import com.weighttracker.data.entity.UserProfile
import com.weighttracker.data.entity.WeightEntry

@Database(
    entities = [WeightEntry::class, Goal::class, UserProfile::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeightDatabase : RoomDatabase() {
    abstract fun weightEntryDao(): WeightEntryDao
    abstract fun goalDao(): GoalDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: WeightDatabase? = null

        fun getDatabase(context: Context): WeightDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeightDatabase::class.java,
                    "weight_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
