package com.weighttracker.data.repository

import com.weighttracker.data.dao.GoalDao
import com.weighttracker.data.dao.UserProfileDao
import com.weighttracker.data.dao.WeightEntryDao
import com.weighttracker.data.entity.Goal
import com.weighttracker.data.entity.UserProfile
import com.weighttracker.data.entity.WeightEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class WeightRepository(
    private val weightEntryDao: WeightEntryDao,
    private val goalDao: GoalDao,
    private val userProfileDao: UserProfileDao
) {
    // Weight Entry operations
    fun getAllEntries(): Flow<List<WeightEntry>> = weightEntryDao.getAllEntries()

    fun getEntriesSince(startDate: LocalDateTime): Flow<List<WeightEntry>> =
        weightEntryDao.getEntriesSince(startDate)

    fun getEntriesBetween(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WeightEntry>> =
        weightEntryDao.getEntriesBetween(startDate, endDate)

    fun getLatestEntry(): Flow<WeightEntry?> = weightEntryDao.getLatestEntry()

    fun getRecentEntries(limit: Int = 10): Flow<List<WeightEntry>> =
        weightEntryDao.getRecentEntries(limit)

    suspend fun insertWeightEntry(entry: WeightEntry): Long {
        return weightEntryDao.insert(entry)
    }

    suspend fun updateWeightEntry(entry: WeightEntry) {
        weightEntryDao.update(entry)
    }

    suspend fun deleteWeightEntry(entry: WeightEntry) {
        weightEntryDao.delete(entry)
    }

    // Goal operations
    fun getAllGoals(): Flow<List<Goal>> = goalDao.getAllGoals()

    fun getActiveGoal(): Flow<Goal?> = goalDao.getActiveGoal()

    suspend fun insertGoal(goal: Goal): Long {
        // Deactivate all previous goals
        goalDao.deactivateAllGoals()
        return goalDao.insert(goal)
    }

    suspend fun updateGoal(goal: Goal) {
        goalDao.update(goal)
    }

    suspend fun deleteGoal(goal: Goal) {
        goalDao.delete(goal)
    }

    // User Profile operations
    fun getUserProfile(): Flow<UserProfile?> = userProfileDao.getUserProfile()

    suspend fun insertUserProfile(profile: UserProfile) {
        userProfileDao.insert(profile)
    }

    suspend fun updateUserProfile(profile: UserProfile) {
        userProfileDao.update(profile)
    }
}
