package com.grocery.billing.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grocery.billing.data.entity.ProductPriceOption

@Dao
interface ProductPriceDao {

    @Query("SELECT * FROM product_prices WHERE product_id = :productId ORDER BY option_id ASC")
    suspend fun getByProduct(productId: String): List<ProductPriceOption>

    @Query("SELECT * FROM product_prices ORDER BY product_id ASC")
    suspend fun getAll(): List<ProductPriceOption>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(option: ProductPriceOption): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(options: List<ProductPriceOption>)

    @Query("DELETE FROM product_prices WHERE product_id = :productId")
    suspend fun deleteByProduct(productId: String)

    @Query("DELETE FROM product_prices")
    suspend fun deleteAll()
}
