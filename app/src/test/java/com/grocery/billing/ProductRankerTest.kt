package com.grocery.billing

import com.grocery.billing.billing.ProductRanker
import com.grocery.billing.data.entity.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductRankerTest {

    private fun product(id: String, name: String, barcode: String? = null) =
        Product(id = id, name = name, barcode = barcode, createdAt = "", updatedAt = "")

    private val products = listOf(
        product("1", "Basmati Rice"),
        product("2", "Rice", "8901000000001"),
        product("3", "Brown Rice"),
        product("4", "Toor Dal"),
        product("5", "Rice Bran Oil")
    )

    @Test
    fun exactMatchFirst() {
        val ranked = ProductRanker.rank("Rice", products)
        assertEquals("Rice", ranked.first().name)
    }

    @Test
    fun exactBarcodeMatchFirst() {
        val ranked = ProductRanker.rank("8901000000001", products)
        assertEquals("Rice", ranked.first().name)
    }

    @Test
    fun prefixMatchesBeforeContains() {
        val ranked = ProductRanker.rank("rice", products)
        val riceIndex = ranked.indexOfFirst { it.name == "Rice" }
        val basmatiIndex = ranked.indexOfFirst { it.name == "Basmati Rice" }
        val brownIndex = ranked.indexOfFirst { it.name == "Brown Rice" }
        val branOilIndex = ranked.indexOfFirst { it.name == "Rice Bran Oil" }
        assertEquals(0, riceIndex)
        // Prefix matches ("Basmati Rice", "Rice Bran Oil") rank above loose contains matches.
        assert(basmatiIndex < brownIndex)
        assert(branOilIndex < brownIndex)
    }

    @Test
    fun nonMatchingProductsRankedLast() {
        val ranked = ProductRanker.rank("dal", products)
        assertEquals("Toor Dal", ranked.first().name)
    }

    @Test
    fun emptyQueryKeepsOrder() {
        val ranked = ProductRanker.rank("", products)
        assertEquals(products, ranked)
    }

    @Test
    fun caseInsensitiveRanking() {
        val ranked = ProductRanker.rank("RICE", products)
        assertEquals("Rice", ranked.first().name)
    }
}
