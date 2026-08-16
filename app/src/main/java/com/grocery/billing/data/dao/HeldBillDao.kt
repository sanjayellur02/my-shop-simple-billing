package com.grocery.billing.data.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.grocery.billing.data.entity.HeldBill
import kotlinx.coroutines.flow.Flow

data class HeldBillWithCount(
    @ColumnInfo(name = "held_bill_id") val heldBillId: Long,
    @ColumnInfo(name = "reference") val reference: String,
    @ColumnInfo(name = "bill_number") val billNumber: String,
    @ColumnInfo(name = "bill_date") val billDate: String,
    @ColumnInfo(name = "bill_time") val billTime: String,
    @ColumnInfo(name = "subtotal") val subtotalPaise: Long,
    @ColumnInfo(name = "discount") val discountPaise: Long,
    @ColumnInfo(name = "total") val totalPaise: Long,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "item_count") val itemCount: Int
)

@Dao
interface HeldBillDao {

    @Insert
    suspend fun insert(bill: HeldBill): Long

    @Update
    suspend fun update(bill: HeldBill)

    @Query("SELECT * FROM held_bills WHERE held_bill_id = :id LIMIT 1")
    suspend fun getById(id: Long): HeldBill?

    @Query(
        """
        SELECT h.held_bill_id, h.reference, h.bill_number, h.bill_date, h.bill_time,
               h.subtotal, h.discount, h.total, h.created_at,
               (SELECT COUNT(*) FROM held_bill_items i WHERE i.held_bill_id = h.held_bill_id) AS item_count
        FROM held_bills h
        ORDER BY h.held_bill_id DESC
        """
    )
    fun observeAllWithCount(): Flow<List<HeldBillWithCount>>

    @Query("SELECT * FROM held_bills ORDER BY held_bill_id DESC")
    fun observeAll(): Flow<List<HeldBill>>

    @Query("SELECT * FROM held_bills ORDER BY held_bill_id ASC")
    suspend fun getAll(): List<HeldBill>

    @Query("UPDATE held_bills SET reference = :reference WHERE held_bill_id = :id")
    suspend fun updateReference(id: Long, reference: String)

    @Query("DELETE FROM held_bills WHERE held_bill_id = :id")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun delete(bill: HeldBill)

    @Query("DELETE FROM held_bills")
    suspend fun deleteAll()
}
