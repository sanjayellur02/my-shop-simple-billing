package com.grocery.billing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Bill items snapshot the product name at billing time so historical bills
 * keep the original name even if the product is renamed or deleted later.
 */
@Entity(
    tableName = "bill_items",
    foreignKeys = [
        ForeignKey(
            entity = Bill::class,
            parentColumns = ["bill_id"],
            childColumns = ["bill_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bill_id")]
)
data class BillItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bill_item_id")
    val billItemId: Long = 0L,
    @ColumnInfo(name = "bill_id")
    val billId: Long,
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
