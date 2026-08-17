package com.grocery.billing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Generic key-value draft store. Each draft is identified by a [key] (e.g.
 * "product_edit:1004" or "bill") and holds a serialised [data] blob plus
 * a timestamp so the UI can show when it was last auto-saved.
 */
@Entity(tableName = "drafts")
data class Draft(
    @PrimaryKey
    @ColumnInfo(name = "draft_key")
    val key: String,

    @ColumnInfo(name = "draft_data")
    val data: String,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String
)
