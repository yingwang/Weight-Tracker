package com.weighttracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val targetWeight: Float, // in kg
    val startWeight: Float, // in kg
    val startDate: LocalDate,
    val targetDate: LocalDate,
    val isActive: Boolean = true
)
