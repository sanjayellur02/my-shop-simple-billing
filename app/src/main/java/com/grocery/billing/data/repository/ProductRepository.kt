package com.grocery.billing.data.repository

import com.grocery.billing.data.dao.ProductDao
import com.grocery.billing.data.dao.ProductPriceDao
import com.grocery.billing.data.entity.Product
import com.grocery.billing.data.entity.ProductPriceOption
import com.grocery.billing.util.Dates
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val dao: ProductDao,
    private val priceDao: ProductPriceDao
) {

    val products: Flow<List<Product>> = dao.observeAll()

    suspend fun search(q: String): List<Product> = dao.search(q)

    suspend fun findExact(q: String): Product? = dao.findExact(q)

    suspend fun recentlySold(limit: Int = 8): List<Product> = dao.recentlySold(limit)

    suspend fun getAll(): List<Product> = dao.getAll()

    suspend fun getById(id: String): Product? = dao.getById(id)

    suspend fun exists(id: String): Boolean = dao.countById(id) > 0

    suspend fun getExtraPrices(productId: String): List<ProductPriceOption> =
        priceDao.getByProduct(productId)

    /** Replaces all extra price options for a product with the given list. */
    suspend fun replaceExtraPrices(productId: String, options: List<ProductPriceOption>) {
        priceDao.deleteByProduct(productId)
        if (options.isNotEmpty()) priceDao.insertAll(options)
    }

    /** Returns error message or null on success. */
    suspend fun add(
        id: String,
        name: String,
        sellingPricePaise: Long = 0L,
        unit: String = "",
        barcode: String? = null
    ): String? {
        val trimmedId = id.trim()
        val trimmedName = name.trim()
        if (trimmedId.isEmpty()) return "Product ID is required."
        if (trimmedName.isEmpty()) return "Product name is required."
        if (dao.countById(trimmedId) > 0) return "Product ID already exists."
        val now = Dates.isoTimestamp()
        dao.insert(
            Product(
                id = trimmedId,
                name = trimmedName,
                sellingPricePaise = sellingPricePaise,
                unit = unit.trim(),
                barcode = barcode?.trim()?.ifEmpty { null },
                createdAt = now,
                updatedAt = now
            )
        )
        return null
    }

    /** Returns error message or null on success. */
    suspend fun update(
        currentId: String,
        newId: String,
        name: String,
        sellingPricePaise: Long = 0L,
        unit: String = "",
        barcode: String? = null
    ): String? {
        val trimmedId = newId.trim()
        val trimmedName = name.trim()
        if (trimmedId.isEmpty()) return "Product ID is required."
        if (trimmedName.isEmpty()) return "Product name is required."
        if (trimmedId != currentId && dao.countById(trimmedId) > 0) return "Product ID already exists."
        val existing = dao.getById(currentId) ?: return "Product not found."
        dao.update(
            existing.copy(
                id = trimmedId,
                name = trimmedName,
                sellingPricePaise = sellingPricePaise,
                unit = unit.trim(),
                barcode = barcode?.trim()?.ifEmpty { null },
                updatedAt = Dates.isoTimestamp()
            )
        )
        return null
    }

    suspend fun delete(product: Product) {
        priceDao.deleteByProduct(product.id)
        dao.delete(product)
    }

    suspend fun insertAll(products: List<Product>, extraOptions: List<ProductPriceOption> = emptyList()) {
        if (products.isEmpty()) return
        dao.insertAll(products)
        if (extraOptions.isNotEmpty()) priceDao.insertAll(extraOptions)
    }
}
