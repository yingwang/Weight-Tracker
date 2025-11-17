package com.weighttracker.utils

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class HealthConnectManager(private val context: Context) {
    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

    companion object {
        val PERMISSIONS = setOf(
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getWritePermission(WeightRecord::class),
            HealthPermission.getReadPermission(WeightRecord::class)
        )
    }

    /**
     * Check if Health Connect is available on this device
     */
    suspend fun isAvailable(): Boolean {
        return HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE
    }

    /**
     * Check if permissions are granted
     */
    suspend fun hasPermissions(): Boolean {
        return try {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            android.util.Log.d("HealthConnectManager", "Granted permissions: $granted")
            android.util.Log.d("HealthConnectManager", "Required permissions: $PERMISSIONS")
            val hasAll = PERMISSIONS.all { it in granted }
            android.util.Log.d("HealthConnectManager", "Has all required permissions: $hasAll")
            hasAll
        } catch (e: Exception) {
            android.util.Log.e("HealthConnectManager", "Error checking permissions", e)
            e.printStackTrace()
            false
        }
    }

    /**
     * Get steps count for today
     */
    suspend fun getTodaySteps(): Long {
        return try {
            android.util.Log.d("HealthConnectManager", "getTodaySteps called")

            // Check if Health Connect is available first
            if (!isAvailable()) {
                android.util.Log.e("HealthConnectManager", "Health Connect is not available")
                throw IllegalStateException("Health Connect is not available on this device")
            }

            // Check permissions
            if (!hasPermissions()) {
                android.util.Log.e("HealthConnectManager", "Health Connect permissions not granted")
                throw SecurityException("Health Connect permissions not granted")
            }

            val today = LocalDateTime.now().toLocalDate()
            val startTime = today.atStartOfDay(ZoneId.systemDefault()).toInstant()
            val endTime = Instant.now()

            android.util.Log.d("HealthConnectManager", "Reading steps from $startTime to $endTime")

            val request = ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )

            val response = healthConnectClient.readRecords(request)
            val totalSteps = response.records.sumOf { it.count }

            android.util.Log.d("HealthConnectManager", "Found ${response.records.size} step records, total: $totalSteps")
            totalSteps
        } catch (e: Exception) {
            android.util.Log.e("HealthConnectManager", "Error getting today's steps", e)
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Get steps count for a specific date range
     */
    suspend fun getStepsForRange(startDate: LocalDateTime, endDate: LocalDateTime): Long {
        return try {
            val startTime = startDate.atZone(ZoneId.systemDefault()).toInstant()
            val endTime = endDate.atZone(ZoneId.systemDefault()).toInstant()

            val request = ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )

            val response = healthConnectClient.readRecords(request)
            response.records.sumOf { it.count }
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }
}
