package com.grocery.billing.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.grocery.billing.data.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY product_id ASC")
    fun observeAll(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY product_id ASC")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM products WHERE product_id = :id LIMIT 1")
    suspend fun getById(id: String): Product?

    @Query("SELECT COUNT(*) FROM products WHERE product_id = :id")
    suspend fun countById(id: String): Int

    @Query("SELECT * FROM products WHERE product_id LIKE '%' || :q || '%' OR product_name LIKE '%' || :q || '%' OR sku LIKE '%' || :q || '%' ORDER BY product_id ASC LIMIT 50")
    suspend fun search(q: String): List<Product>

    @Query("SELECT * FROM products WHERE product_id = :q OR product_name = :q OR barcode = :q OR sku = :q LIMIT 1")
    suspend fun findExact(q: String): Product?

    @Query("SELECT COUNT(*) FROM products WHERE sku = :sku")
    suspend fun countBySku(sku: String): Int

    @Query("SELECT sku FROM products WHERE sku IS NOT NULL")
    suspend fun getAllSkus(): List<String>

    @Query("SELECT barcode FROM products WHERE barcode IS NOT NULL")
    suspend fun getAllBarcodes(): List<String>

    /** Products that appear in completed bills, most recently used first. */
    @Query(
        """
        SELECT p.* FROM products p
        INNER JOIN (
            SELECT product_id, COUNT(*) AS sold_count, MAX(bill_id) AS last_bill_id
            FROM bill_items
            WHERE product_id IS NOT NULL AND product_id != ''
            GROUP BY product_id
        ) s ON p.product_id = s.product_id
        ORDER BY s.last_bill_id DESC, s.sold_count DESC
        LIMIT :limit
        """
    )
    suspend fun recentlySold(limit: Int): List<Product>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(products: List<Product>)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM products WHERE product_id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}
