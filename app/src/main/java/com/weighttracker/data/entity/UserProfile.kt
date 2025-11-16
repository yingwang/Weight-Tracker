package com.weighttracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Long = 1, // Single user profile
    val height: Float, // in cm
    val age: Int,
    val gender: String, // "male" or "female"
    val preferredUnit: String = "kg" // "kg" or "lbs"
)
