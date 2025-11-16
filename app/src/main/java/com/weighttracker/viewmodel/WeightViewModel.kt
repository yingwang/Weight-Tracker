package com.weighttracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weighttracker.data.entity.Goal
import com.weighttracker.data.entity.UserProfile
import com.weighttracker.data.entity.WeightEntry
import com.weighttracker.data.repository.WeightRepository
import com.weighttracker.utils.BMICalculator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class WeightViewModel(
    private val repository: WeightRepository
) : ViewModel() {

    // State flows
    val allEntries: StateFlow<List<WeightEntry>> = repository.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentEntries: StateFlow<List<WeightEntry>> = repository.getRecentEntries(10)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestEntry: StateFlow<WeightEntry?> = repository.getLatestEntry()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeGoal: StateFlow<Goal?> = repository.getActiveGoal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userProfile: StateFlow<UserProfile?> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Computed properties
    val currentBMI: StateFlow<Float?> = combine(latestEntry, userProfile) { entry, profile ->
        if (entry != null && profile != null) {
            BMICalculator.calculate(entry.weight, profile.height)
        } else null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val goalProgress: StateFlow<Float> = combine(latestEntry, activeGoal) { entry, goal ->
        if (entry != null && goal != null) {
            val totalChange = goal.targetWeight - goal.startWeight
            val currentChange = entry.weight - goal.startWeight
            if (totalChange != 0f) {
                (currentChange / totalChange * 100f).coerceIn(0f, 100f)
            } else 0f
        } else 0f
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    // Actions
    fun addWeightEntry(weight: Float, note: String = "") {
        viewModelScope.launch {
            val profile = userProfile.value
            val bmi = profile?.let { BMICalculator.calculate(weight, it.height) }
            val entry = WeightEntry(
                weight = weight,
                timestamp = LocalDateTime.now(),
                note = note,
                bmi = bmi
            )
            repository.insertWeightEntry(entry)
        }
    }

    fun updateWeightEntry(entry: WeightEntry) {
        viewModelScope.launch {
            repository.updateWeightEntry(entry)
        }
    }

    fun deleteWeightEntry(entry: WeightEntry) {
        viewModelScope.launch {
            repository.deleteWeightEntry(entry)
        }
    }

    fun setGoal(targetWeight: Float, targetDate: java.time.LocalDate) {
        viewModelScope.launch {
            val currentWeight = latestEntry.value?.weight ?: return@launch
            val goal = Goal(
                targetWeight = targetWeight,
                startWeight = currentWeight,
                startDate = java.time.LocalDate.now(),
                targetDate = targetDate,
                isActive = true
            )
            repository.insertGoal(goal)
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            repository.updateGoal(goal)
        }
    }

    fun saveUserProfile(height: Float, age: Int, gender: String, preferredUnit: String = "kg") {
        viewModelScope.launch {
            val profile = UserProfile(
                height = height,
                age = age,
                gender = gender,
                preferredUnit = preferredUnit
            )
            repository.insertUserProfile(profile)
        }
    }

    fun updateUserProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateUserProfile(profile)
        }
    }

    fun getEntriesForPeriod(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WeightEntry>> {
        return repository.getEntriesBetween(startDate, endDate)
    }
}
