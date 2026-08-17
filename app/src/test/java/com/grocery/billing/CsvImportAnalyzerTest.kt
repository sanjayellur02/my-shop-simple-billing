package com.grocery.billing

import com.grocery.billing.data.csv.CsvImportAnalyzer
import com.grocery.billing.data.csv.CsvParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class CsvImportAnalyzerTest {

    private fun analyze(text: String, existing: Set<String> = emptySet()) =
        CsvImportAnalyzer.analyze(CsvParser.parse(text), existing)

    @Test
    fun validFileAnalyzes() {
        val result = analyze("id,product_name\n101,Rice\n102,Sugar\n103,Sunflower Oil")
        assertNull(result.headerError)
        assertEquals(3, result.totalRows)
        assertEquals(3, result.valid.size)
        assertEquals(0, result.duplicateCount)
        assertEquals(0, result.invalidCount)
    }

    @Test
    fun missingColumnsRejected() {
        val result = analyze("name,price\nRice,60")
        assertNotNull(result.headerError)
        assertEquals(0, result.totalRows)
    }

    @Test
    fun emptyFileRejected() {
        val result = analyze("")
        assertNotNull(result.headerError)
    }

    @Test
    fun duplicateIdsWithinFileDetected() {
        val result = analyze("id,product_name\n101,Rice\n102,Sugar\n101,Another Rice")
        assertEquals(3, result.totalRows)
        assertEquals(2, result.valid.size)
        assertEquals(1, result.duplicateCount)
    }

    @Test
    fun existingIdsMarkedDuplicate() {
        val result = analyze("id,product_name\n101,Rice\n102,Sugar", existing = setOf("101"))
        assertEquals(2, result.totalRows)
        assertEquals(1, result.valid.size)
        assertEquals(1, result.duplicateCount)
    }

    @Test
    fun invalidRowsCounted() {
        val result = analyze("id,product_name\n101,Rice\n,Rice2\n102,\n103,Sugar")
        assertEquals(4, result.totalRows)
        assertEquals(2, result.valid.size)
        assertEquals(2, result.invalidCount)
    }

    @Test
    fun caseInsensitiveHeader() {
        val result = analyze("ID,Product_Name\n101,Rice")
        assertNull(result.headerError)
        assertEquals(1, result.valid.size)
        assertEquals("101", result.valid[0].id)
        assertEquals("Rice", result.valid[0].name)
    }

    @Test
    fun blankLinesIgnored() {
        val result = analyze("id,product_name\n\n101,Rice\n\n102,Sugar\n")
        assertEquals(2, result.totalRows)
        assertEquals(2, result.valid.size)
    }

    @Test
    fun optionalPriceUnitBarcodeParsed() {
        val result = analyze("id,product_name,price,unit,barcode\n101,Rice,70,kg,8901\n102,Sugar,45,,x")
        assertEquals(2, result.valid.size)
        assertEquals(7000L, result.valid[0].sellingPricePaise)
        assertEquals("kg", result.valid[0].unit)
        assertEquals("8901", result.valid[0].barcode)
        assertEquals(4500L, result.valid[1].sellingPricePaise)
        assertEquals("", result.valid[1].unit)
    }

    @Test
    fun missingOptionalColumnsDefault() {
        val result = analyze("id,product_name\n101,Rice")
        assertEquals(0L, result.valid[0].sellingPricePaise)
        assertEquals("", result.valid[0].unit)
        assertEquals(null, result.valid[0].barcode)
    }

    @Test
    fun emptyBarcodeBecomesNull() {
        val result = analyze("id,product_name,barcode\n101,Rice,")
        assertEquals(null, result.valid[0].barcode)
    }

    @Test
    fun invalidPriceRejectsRow() {
        val result = analyze("id,product_name,price\n101,Rice,abc\n102,Sugar,50")
        assertEquals(1, result.valid.size)
        assertEquals(1, result.invalidCount)
        assertEquals(5000L, result.valid[0].sellingPricePaise)
    }

    @Test
    fun repeatedIdWithPriceAddsExtraOption() {
        val result = analyze("id,product_name,price,unit\n101,Rice,70,kg\n101,Rice,140,2kg")
        assertEquals(2, result.totalRows)
        assertEquals(1, result.valid.size)
        assertEquals(0, result.duplicateCount)
        assertEquals(7000L, result.valid[0].sellingPricePaise)
        assertEquals("kg", result.valid[0].unit)
        assertEquals(1, result.valid[0].extraOptions.size)
        assertEquals(14000L, result.valid[0].extraOptions[0].sellingPricePaise)
        assertEquals("2kg", result.valid[0].extraOptions[0].unit)
    }

    @Test
    fun repeatedIdWithoutPriceBecomesExtraOption() {
        val result = analyze("id,product_name,price\n101,Rice,70\n101,Rice")
        assertEquals(1, result.valid.size)
        assertEquals(0, result.duplicateCount)
        assertEquals(1, result.valid[0].extraOptions.size)
        assertEquals(0L, result.valid[0].extraOptions[0].sellingPricePaise)
    }

    @Test
    fun repeatedIdSameUnitAndPriceIsDuplicate() {
        val result = analyze("id,product_name,price,unit\n101,Rice,70,kg\n101,Rice,70,kg")
        assertEquals(1, result.valid.size)
        assertEquals(1, result.duplicateCount)
        assertEquals(0, result.valid[0].extraOptions.size)
    }

    @Test
    fun repeatedIdSameUnitDifferentPriceIsExtraOption() {
        val result = analyze("id,product_name,price,unit\n101,Rice,70,kg\n101,Rice,140,kg")
        assertEquals(1, result.valid.size)
        assertEquals(0, result.duplicateCount)
        assertEquals(1, result.valid[0].extraOptions.size)
        assertEquals(14000L, result.valid[0].extraOptions[0].sellingPricePaise)
    }

    @Test
    fun repeatedIdDifferentUnitNoPriceIsExtraOption() {
        val result = analyze("id,product_name,price,unit\n101,Rice,,kg\n101,Rice,,1/2kg")
        assertEquals(1, result.valid.size)
        assertEquals(0, result.duplicateCount)
        assertEquals(1, result.valid[0].extraOptions.size)
        assertEquals("1/2kg", result.valid[0].extraOptions[0].unit)
    }

    @Test
    fun newCsvFormatWithSkuAndRate() {
        val result = analyze("ID,Product Name,Rate,SKU,Barcode\n1,Akki,,,\n2,Colgate 200g,110,,89012345")
        assertEquals(2, result.valid.size)
        assertEquals("1", result.valid[0].id)
        assertEquals("Akki", result.valid[0].name)
        assertNull(result.valid[0].sku)
        assertNull(result.valid[0].barcode)
        assertEquals("2", result.valid[1].id)
        assertEquals("Colgate 200g", result.valid[1].name)
        assertEquals(11000L, result.valid[1].sellingPricePaise)
        assertNull(result.valid[1].sku)
        assertEquals("89012345", result.valid[1].barcode)
    }

    @Test
    fun newCsvFormatWithSkuProvided() {
        val result = analyze("ID,Product Name,Rate,SKU,Barcode\n1,Colgate 200g,110,COL-200G,")
        assertEquals(1, result.valid.size)
        assertEquals("COL-200G", result.valid[0].sku)
        assertNull(result.valid[0].barcode)
    }

    @Test
    fun newCsvFormatHeaderCaseInsensitive() {
        val result = analyze("id,product name,rate,sku,barcode\n1,Akki,,,")
        assertEquals(1, result.valid.size)
        assertEquals("Akki", result.valid[0].name)
    }

    @Test
    fun oldFormatStillWorks() {
        val result = analyze("id,product_name,price,unit,barcode\n101,Rice,70,kg,8901")
        assertEquals(1, result.valid.size)
        assertEquals("101", result.valid[0].id)
        assertEquals(7000L, result.valid[0].sellingPricePaise)
        assertEquals("kg", result.valid[0].unit)
        assertEquals("8901", result.valid[0].barcode)
    }

    @Test
    fun repeatedIdWithNewFormatAddsExtraOption() {
        val result = analyze("ID,Product Name,Rate,SKU,Barcode\n101,Rice,70,,\n101,Rice,140,,")
        assertEquals(2, result.totalRows)
        assertEquals(1, result.valid.size)
        assertEquals(0, result.duplicateCount)
        assertEquals(7000L, result.valid[0].sellingPricePaise)
        assertEquals(1, result.valid[0].extraOptions.size)
        assertEquals(14000L, result.valid[0].extraOptions[0].sellingPricePaise)
    }
}
