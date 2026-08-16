package com.grocery.billing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Snapshot of one product line inside a held (waiting customer) bill.
 * Mirrors [BillItem] so resumed bills restore exactly what was parked.
 */
@Entity(
    tableName = "held_bill_items",
    foreignKeys = [
        ForeignKey(
            entity = HeldBill::class,
            parentColumns = ["held_bill_id"],
            childColumns = ["held_bill_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("held_bill_id")]
)
data class HeldBillItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "held_bill_item_id")
    val heldBillItemId: Long = 0L,
    @ColumnInfo(name = "held_bill_id")
    val heldBillId: Long,
    @ColumnInfo(name = "product_id")
    val productId: String?,
    @ColumnInfo(name = "product_name_snapshot")
    val productNameSnapshot: String,
    @ColumnInfo(name = "quantity")
    val quantity: String,
    @ColumnInfo(name = "rate")
    val ratePaise: Long,
    @ColumnInfo(name = "amount")
    val amountPaise: Long
)
