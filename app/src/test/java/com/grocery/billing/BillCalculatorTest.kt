package com.grocery.billing

import com.grocery.billing.billing.BillCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BillCalculatorTest {

    @Test
    fun amountTwoTimesSixty() {
        assertEquals(12000L, BillCalculator.itemAmountPaise("2", 6000))
    }

    @Test
    fun amountDecimalQuantity() {
        assertEquals(15000L, BillCalculator.itemAmountPaise("2.5", 6000))
        assertEquals(5000L, BillCalculator.itemAmountPaise("0.5", 10000))
    }

    @Test
    fun amountDecimalRate() {
        assertEquals(13650L, BillCalculator.itemAmountPaise("3", 4550))
        assertEquals(12625L, BillCalculator.itemAmountPaise("2.5", 5050))
    }

    @Test
    fun roundingNearestPaise() {
        // Rates are integer paise, so these are exact: 3 x 33 = 99 paise, etc.
        assertEquals(99L, BillCalculator.itemAmountPaise("3", 33))
        assertEquals(33L, BillCalculator.itemAmountPaise("1", 33))
        assertEquals(66L, BillCalculator.itemAmountPaise("2", 33))
    }

    @Test
    fun subtotalSumsAmounts() {
        assertEquals(42500L, BillCalculator.subtotalPaise(listOf(12000, 4500, 26000)))
    }

    @Test
    fun grandTotalWithDiscount() {
        assertEquals(40000L, BillCalculator.grandTotalPaise(42500, 2500))
    }

    @Test
    fun grandTotalNeverNegative() {
        assertEquals(0L, BillCalculator.grandTotalPaise(1000, 5000))
    }

    @Test
    fun quantityValidation() {
        assertTrue(BillCalculator.isValidQuantity("1"))
        assertTrue(BillCalculator.isValidQuantity("2.5"))
        assertTrue(BillCalculator.isValidQuantity("0.5"))
        assertFalse(BillCalculator.isValidQuantity(""))
        assertFalse(BillCalculator.isValidQuantity("0"))
        assertFalse(BillCalculator.isValidQuantity("0.0"))
        assertFalse(BillCalculator.isValidQuantity("-2"))
        assertFalse(BillCalculator.isValidQuantity("abc"))
    }

    @Test
    fun rateValidation() {
        assertTrue(BillCalculator.isValidRatePaise(0))
        assertTrue(BillCalculator.isValidRatePaise(6000))
        assertFalse(BillCalculator.isValidRatePaise(-100))
        assertTrue(BillCalculator.isValidRateText("60"))
        assertTrue(BillCalculator.isValidRateText("50.50"))
        assertFalse(BillCalculator.isValidRateText("-5"))
        assertFalse(BillCalculator.isValidRateText(""))
    }
}
