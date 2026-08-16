package com.grocery.billing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Product master. The selling price / unit / barcode are optional and are
 * used to pre-fill the rate during billing; the cashier can override it if
 * the shop has allowed price overrides.
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val id: String,
    @ColumnInfo(name = "product_name")
    val name: String,
    @ColumnInfo(name = "selling_price")
    val sellingPricePaise: Long = 0L,
    @ColumnInfo(name = "unit")
    val unit: String = "",
    @ColumnInfo(name = "barcode")
    val barcode: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String
)
