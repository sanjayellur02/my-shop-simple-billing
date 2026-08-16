package com.grocery.billing

import com.grocery.billing.util.BillNumbers
import org.junit.Assert.assertEquals
import org.junit.Test

class BillNumbersTest {

    @Test
    fun formatsSequential() {
        assertEquals("000001", BillNumbers.format(1))
        assertEquals("000123", BillNumbers.format(123))
        assertEquals("000999", BillNumbers.format(999))
        assertEquals("001000", BillNumbers.format(1000))
        assertEquals("999999", BillNumbers.format(999999))
        assertEquals("1000000", BillNumbers.format(1000000))
    }

    @Test
    fun nextNumberStartsAtOne() {
        assertEquals(1L, BillNumbers.nextNumber(emptyList()))
    }

    @Test
    fun nextNumberContinuesSequence() {
        val existing = listOf("000001", "000002", "000003")
        assertEquals(4L, BillNumbers.nextNumber(existing))
    }

    @Test
    fun nextNumberIsMaxPlusOne() {
        val existing = listOf("000001", "000005", "000010")
        assertEquals(11L, BillNumbers.nextNumber(existing))
    }

    @Test
    fun nextNumberHandlesMissingDigits() {
        val existing = listOf("", "abc")
        assertEquals(1L, BillNumbers.nextNumber(existing))
    }
}
