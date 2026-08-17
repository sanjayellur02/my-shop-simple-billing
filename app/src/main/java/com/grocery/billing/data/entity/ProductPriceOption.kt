package com.grocery.billing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * An additional selling price for a product. The product's own
 * [Product.sellingPricePaise]/[Product.unit] fields hold the default price;
 * this table holds extra price options (each with its own unit). Barcode is
 * shared across all options of a product.
 */
@Entity(
    tableName = "product_prices",
    indices = [Index(value = ["product_id"])],
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class ProductPriceOption(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "option_id")
    val optionId: Long = 0,
    @ColumnInfo(name = "product_id")
    val productId: String,
    @ColumnInfo(name = "selling_price_paise")
    val sellingPricePaise: Long,
    @ColumnInfo(name = "unit", defaultValue = "")
    val unit: String = ""
)
