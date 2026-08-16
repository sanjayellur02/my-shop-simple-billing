package com.grocery.billing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bill_id")
    val billId: Long = 0L,
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
