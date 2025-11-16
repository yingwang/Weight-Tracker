package com.weighttracker.data.dao

import androidx.room.*
import com.weighttracker.data.entity.WeightEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface WeightEntryDao {
    @Query("SELECT * FROM weight_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries WHERE timestamp >= :startDate ORDER BY timestamp DESC")
    fun getEntriesSince(startDate: LocalDateTime): Flow<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries WHERE timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp ASC")
    fun getEntriesBetween(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries ORDER BY timestamp DESC LIMIT 1")
    fun getLatestEntry(): Flow<WeightEntry?>

    @Query("SELECT * FROM weight_entries ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentEntries(limit: Int): Flow<List<WeightEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: WeightEntry): Long

    @Update
    suspend fun update(entry: WeightEntry)

    @Delete
    suspend fun delete(entry: WeightEntry)

    @Query("DELETE FROM weight_entries")
    suspend fun deleteAll()
}
