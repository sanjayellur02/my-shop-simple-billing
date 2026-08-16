package com.grocery.billing

import com.grocery.billing.data.csv.CsvParser
import org.junit.Assert.assertEquals
import org.junit.Test

class CsvParserTest {

    @Test
    fun parsesSimpleRows() {
        val rows = CsvParser.parse("id,product_name\n101,Rice\n102,Sugar")
        assertEquals(3, rows.size)
        assertEquals(listOf("id", "product_name"), rows[0])
        assertEquals(listOf("101", "Rice"), rows[1])
        assertEquals(listOf("102", "Sugar"), rows[2])
    }

    @Test
    fun parsesQuotedFields() {
        val rows = CsvParser.parse("id,product_name\n103,\"Sunflower Oil\"\n104,Soap")
        assertEquals("Sunflower Oil", rows[1][1])
    }

    @Test
    fun parsesCommaInsideQuotes() {
        val rows = CsvParser.parse("id,product_name\n105,\"Rice, Basmati\"")
        assertEquals(listOf("105", "Rice, Basmati"), rows[1])
    }

    @Test
    fun parsesEscapedQuotes() {
        val rows = CsvParser.parse("id,product_name\n106,\"He said \"\"Hi\"\"\"")
        assertEquals("He said \"Hi\"", rows[1][1])
    }

    @Test
    fun handlesTrailingNewline() {
        val rows = CsvParser.parse("id,name\n101,Rice\n")
        assertEquals(2, rows.size)
    }

    @Test
    fun emptyFileGivesEmptyRows() {
        val rows = CsvParser.parse("")
        assertEquals(0, rows.size)
    }
}
