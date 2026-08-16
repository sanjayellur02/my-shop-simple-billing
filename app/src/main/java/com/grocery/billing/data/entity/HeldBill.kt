package com.grocery.billing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A bill parked for a waiting customer. Keeps the bill number, date and all
 * totals exactly as they were when the cashier pressed "Hold Bill". Completed
 * bills live in the [Bill] table; held bills stay here until resumed or deleted.
 */
@Entity(tableName = "held_bills")
data class HeldBill(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "held_bill_id")
    val heldBillId: Long = 0L,
    @ColumnInfo(name = "reference")
    val reference: String,
    @ColumnInfo(name = "bill_number")
    val billNumber: String,
    @ColumnInfo(name = "bill_date")
    val billDate: String,
    @ColumnInfo(name = "bill_time")
    val billTime: String,
    @ColumnInfo(name = "subtotal")
    val subtotalPaise: Long,
    @ColumnInfo(name = "discount")
    val discountPaise: Long,
    @ColumnInfo(name = "total")
    val totalPaise: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: String
)
