package com.grocery.billing

import com.grocery.billing.util.BarcodeNumberGenerator
import com.grocery.billing.util.SkuGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SkuGeneratorTest {

    @Test
    fun singleWordShort() {
        assertEquals("AKKI", SkuGenerator.generate("Akki"))
    }

    @Test
    fun singleWordShort2() {
        assertEquals("GODHI", SkuGenerator.generate("Godhi"))
    }

    @Test
    fun singleWordShort3() {
        assertEquals("SABBU", SkuGenerator.generate("Sabbu"))
    }

    @Test
    fun singleWordShort4() {
        assertEquals("UPPU", SkuGenerator.generate("Uppu"))
    }

    @Test
    fun singleWordLongNoSuffix() {
        assertEquals("SAKKARE", SkuGenerator.generate("Sakkare"))
    }

    @Test
    fun multiWordNoSuffix() {
        assertEquals("TOGARI-BELE", SkuGenerator.generate("Togari Bele"))
    }

    @Test
    fun multiWordNoSuffix2() {
        assertEquals("HESARU-BELE", SkuGenerator.generate("Hesaru Bele"))
    }

    @Test
    fun multiWordNoSuffix3() {
        assertEquals("UDDINA-BELE", SkuGenerator.generate("Uddina Bele"))
    }

    @Test
    fun brandWithSizeSuffix() {
        assertEquals("COL-200G", SkuGenerator.generate("Colgate 200g"))
    }

    @Test
    fun brandWithSizeSuffix2() {
        assertEquals("COL-100G", SkuGenerator.generate("Colgate 100g"))
    }

    @Test
    fun brandWithSizeSuffix3() {
        assertEquals("COL-50G", SkuGenerator.generate("Colgate 50g"))
    }

    @Test
    fun brandWithHyphenAndPriceSuffix() {
        assertEquals("PARLE-G-5RS", SkuGenerator.generate("Parle-G 5rs"))
    }

    @Test
    fun brandWithHyphenAndPriceSuffix2() {
        assertEquals("PARLE-G-10RS", SkuGenerator.generate("Parle-G 10rs"))
    }

    @Test
    fun shortBrandWithPriceSuffix() {
        assertEquals("MAGGI-12RS", SkuGenerator.generate("Maggi 12rs"))
    }

    @Test
    fun shortBrandWithPriceSuffix2() {
        assertEquals("MAGGI-30RS", SkuGenerator.generate("Maggi 30rs"))
    }

    @Test
    fun multiWordBrandWithSize() {
        assertEquals("SUNFLOWER-OIL-1L", SkuGenerator.generate("Sunflower Oil 1L"))
    }

    @Test
    fun emptyName() {
        assertEquals("", SkuGenerator.generate(""))
    }

    @Test
    fun whitespaceOnly() {
        assertEquals("", SkuGenerator.generate("   "))
    }

    @Test
    fun mixedCaseUpperLower() {
        assertEquals("CHIPS-5RS", SkuGenerator.generate("Chips 5rs"))
    }

    @Test
    fun stability() {
        val sku1 = SkuGenerator.generate("Colgate 200g")
        val sku2 = SkuGenerator.generate("Colgate 200g")
        assertEquals(sku1, sku2)
    }

    @Test
    fun uniquenessForSimilarProducts() {
        val sku1 = SkuGenerator.generate("Colgate 200g")
        val sku2 = SkuGenerator.generate("Colgate 100g")
        val sku3 = SkuGenerator.generate("Colgate 50g")
        assertNotEquals(sku1, sku2)
        assertNotEquals(sku1, sku3)
        assertNotEquals(sku2, sku3)
    }

    @Test
    fun looseProductNoAutoUnit() {
        assertEquals("AKKI", SkuGenerator.generate("Akki"))
    }

    @Test
    fun looseProductWithExplicitUnit() {
        assertEquals("AKKI-1KG", SkuGenerator.generate("Akki 1kg"))
    }

    @Test
    fun threeWordProduct() {
        assertEquals("BASMATI-RICE-5KG", SkuGenerator.generate("Basmati Rice 5kg"))
    }
}

class BarcodeNumberGeneratorTest {

    @Test
    fun generates8Digits() {
        val code = BarcodeNumberGenerator.generate(emptySet())
        assertEquals(8, code.length)
        assertTrue(code.all { it.isDigit() })
    }

    @Test
    fun uniqueAgainstExisting() {
        val existing = (1..100).map { (10_000_000..99_999_999).random().toString() }.toSet()
        val code = BarcodeNumberGenerator.generate(existing)
        assertTrue(code !in existing)
    }

    @Test
    fun batchGeneratesUniqueCodes() {
        val batch = BarcodeNumberGenerator.generateBatch(50, emptySet())
        assertEquals(50, batch.size)
        assertEquals(50, batch.toSet().size)
        assertTrue(batch.all { it.length == 8 && it.all { c -> c.isDigit() } })
    }

    @Test
    fun batchRespectsExisting() {
        val existing = setOf("12345678", "87654321")
        val batch = BarcodeNumberGenerator.generateBatch(10, existing)
        assertTrue(batch.all { it !in existing })
        assertEquals(10, batch.toSet().size)
    }

    @Test
    fun largeBatch() {
        val batch = BarcodeNumberGenerator.generateBatch(500, emptySet())
        assertEquals(500, batch.toSet().size)
    }
}
