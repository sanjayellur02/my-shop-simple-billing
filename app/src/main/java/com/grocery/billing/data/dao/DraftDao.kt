package com.grocery.billing.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grocery.billing.data.entity.Draft

@Dao
interface DraftDao {

    @Query("SELECT * FROM drafts WHERE draft_key = :key LIMIT 1")
    suspend fun get(key: String): Draft?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(draft: Draft)

    @Query("DELETE FROM drafts WHERE draft_key = :key")
    suspend fun delete(key: String)

    @Query("DELETE FROM drafts")
    suspend fun deleteAll()
}
