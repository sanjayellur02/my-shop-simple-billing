package com.grocery.billing.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grocery.billing.data.entity.Setting
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings")
    fun observeAll(): Flow<List<Setting>>

    @Query("SELECT * FROM settings")
    suspend fun getAll(): List<Setting>

    @Query("SELECT value FROM settings WHERE `key` = :key LIMIT 1")
    suspend fun get(key: String): String?

    @Query("SELECT value FROM settings WHERE `key` = :key LIMIT 1")
    fun observe(key: String): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(setting: Setting)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putAll(settings: List<Setting>)

    @Query("DELETE FROM settings")
    suspend fun deleteAll()
}
