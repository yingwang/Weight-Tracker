package com.weighttracker.utils

import android.content.Context
import android.util.Log
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
        private const val TAG = "HealthConnectManager"

        val PERMISSIONS = setOf(
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getWritePermission(WeightRecord::class),
            HealthPermission.getReadPermission(WeightRecord::class)
        )
    }

    /**
     * Check if Health Connect app is installed on the device
     */
    fun isHealthConnectAppInstalled(): Boolean {
        return try {
            val packageManager = context.packageManager
            val packageName = "com.google.android.apps.healthdata"
            packageManager.getPackageInfo(packageName, 0)
            Log.d(TAG, "Health Connect app is installed: $packageName")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Health Connect app is not installed", e)
            false
        }
    }

    /**
     * Check if Health Connect is available on this device
     */
    suspend fun isAvailable(): Boolean {
        val status = HealthConnectClient.getSdkStatus(context)
        val available = status == HealthConnectClient.SDK_AVAILABLE
        val appInstalled = isHealthConnectAppInstalled()
        Log.d(TAG, "Health Connect availability check - Status: $status, Available: $available, App installed: $appInstalled")
        return available
    }

    /**
     * Check if permissions are granted
     */
    suspend fun hasPermissions(): Boolean {
        return try {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            val hasAll = PERMISSIONS.all { it in granted }
            Log.d(TAG, "Granted permissions: $granted")
            Log.d(TAG, "Required permissions: $PERMISSIONS")
            Log.d(TAG, "Has all required permissions: $hasAll")
            hasAll
        } catch (e: Exception) {
            Log.e(TAG, "Error checking permissions", e)
            e.printStackTrace()
            false
        }
    }

    /**
     * Get steps count for today
     */
    suspend fun getTodaySteps(): Long {
        return try {
            val today = LocalDateTime.now().toLocalDate()
            val startTime = today.atStartOfDay(ZoneId.systemDefault()).toInstant()
            val endTime = Instant.now()

            val request = ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )

            val response = healthConnectClient.readRecords(request)
            response.records.sumOf { it.count }
        } catch (e: Exception) {
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
