package com.grocery.billing.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.grocery.billing.data.entity.HeldBillItem

@Dao
interface HeldBillItemDao {

    @Insert
    suspend fun insertAll(items: List<HeldBillItem>)

    @Query("SELECT * FROM held_bill_items WHERE held_bill_id = :heldBillId ORDER BY held_bill_item_id ASC")
    suspend fun getByHeldBill(heldBillId: Long): List<HeldBillItem>

    @Query("SELECT * FROM held_bill_items ORDER BY held_bill_item_id ASC")
    suspend fun getAll(): List<HeldBillItem>

    @Query("DELETE FROM held_bill_items")
    suspend fun deleteAll()
}
