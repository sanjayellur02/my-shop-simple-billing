package com.grocery.billing.billing

import com.grocery.billing.data.entity.Product

/**
 * Ranks product search results for the billing screen so exact matches and
 * beginning-of-name matches appear before loose "contains" matches.
 */
object ProductRanker {

    fun rank(query: String, products: List<Product>): List<Product> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return products
        return products.sortedWith(
            compareBy(
                { rankOf(it, q) },
                { it.name.lowercase() },
                { it.id.lowercase() }
            )
        )
    }

    private fun rankOf(product: Product, q: String): Int {
        val id = product.id.lowercase()
        val name = product.name.lowercase()
        val barcode = product.barcode?.lowercase()
        val sku = product.sku?.lowercase()
        return when {
            id == q || name == q || barcode == q || sku == q -> 0
            id.startsWith(q) || name.startsWith(q) || (barcode != null && barcode.startsWith(q)) || (sku != null && sku.startsWith(q)) -> 1
            id.contains(q) || name.contains(q) || (barcode != null && barcode.contains(q)) || (sku != null && sku.contains(q)) -> 2
            else -> 3
        }
    }
}
