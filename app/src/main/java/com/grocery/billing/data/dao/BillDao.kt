package com.grocery.billing.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.grocery.billing.data.entity.Bill
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {

    @Insert
    suspend fun insert(bill: Bill): Long

    @Query("SELECT * FROM bills ORDER BY bill_id DESC")
    fun observeAll(): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE bill_id = :billId LIMIT 1")
    suspend fun getById(billId: Long): Bill?

    @Query("SELECT * FROM bills WHERE bill_id = :billId LIMIT 1")
    fun observeById(billId: Long): Flow<Bill?>

    @Query("SELECT * FROM bills WHERE bill_date = :date ORDER BY bill_id DESC")
    fun observeByDate(date: String): Flow<List<Bill>>

    @Query("SELECT bill_number FROM bills")
    suspend fun allBillNumbers(): List<String>

    @Query("SELECT * FROM bills ORDER BY bill_id ASC")
    suspend fun getAll(): List<Bill>

    @Query("SELECT * FROM bills WHERE bill_number LIKE '%' || :q || '%' OR bill_date LIKE '%' || :q || '%' ORDER BY bill_id DESC")
    fun search(q: String): Flow<List<Bill>>

    @Query("DELETE FROM bills")
    suspend fun deleteAll()
}
