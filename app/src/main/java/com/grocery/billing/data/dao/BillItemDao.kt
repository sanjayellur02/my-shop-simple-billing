package com.grocery.billing.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.grocery.billing.data.entity.BillItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BillItemDao {

    @Insert
    suspend fun insertAll(items: List<BillItem>)

    @Query("SELECT * FROM bill_items WHERE bill_id = :billId")
    fun observeByBill(billId: Long): Flow<List<BillItem>>

    @Query("SELECT * FROM bill_items WHERE bill_id = :billId")
    suspend fun getByBill(billId: Long): List<BillItem>

    @Query("SELECT * FROM bill_items ORDER BY bill_item_id ASC")
    suspend fun getAll(): List<BillItem>

    @Query("DELETE FROM bill_items")
    suspend fun deleteAll()
}
