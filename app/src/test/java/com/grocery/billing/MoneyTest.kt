package com.grocery.billing

import com.grocery.billing.money.Money
import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyTest {

    @Test
    fun parsesRupeesToPaise() {
        assertEquals(6000L, Money.parseRupeesToPaise("60"))
        assertEquals(5050L, Money.parseRupeesToPaise("50.50"))
        assertEquals(12575L, Money.parseRupeesToPaise("125.75"))
        assertEquals(100000L, Money.parseRupeesToPaise("1000"))
        assertEquals(5050L, Money.parseRupeesToPaise("₹50.50"))
        assertEquals(5050L, Money.parseRupeesToPaise(" 50.50 "))
        assertEquals(1000000L, Money.parseRupeesToPaise("10,000"))
        assertEquals(5050L, Money.parseRupeesToPaise("Rs50.50"))
    }

    @Test
    fun rejectsInvalidInput() {
        assertEquals(null, Money.parseRupeesToPaise(""))
        assertEquals(null, Money.parseRupeesToPaise("abc"))
        assertEquals(null, Money.parseRupeesToPaise("-"))
    }

    @Test
    fun roundsHalfUp() {
        assertEquals(123L, Money.parseRupeesToPaise("1.234"))
        assertEquals(124L, Money.parseRupeesToPaise("1.235"))
    }

    @Test
    fun formatsPaise() {
        assertEquals("₹60", Money.paiseToDisplay(6000))
        assertEquals("₹50.50", Money.paiseToDisplay(5050))
        assertEquals("₹125.75", Money.paiseToDisplay(12575))
        assertEquals("₹12,450", Money.paiseToDisplay(1245000))
        assertEquals("₹15,450", Money.paiseToDisplay(1545000))
        assertEquals("₹1,23,456", Money.paiseToDisplay(12345600))
        assertEquals("₹0", Money.paiseToDisplay(0))
    }

    @Test
    fun formatsWithoutSymbol() {
        assertEquals("12,450", Money.paiseToNumber(1245000))
        assertEquals("50.50", Money.paiseToNumber(5050))
    }

    @Test
    fun indianGrouping() {
        assertEquals("1", Money.indianGrouping("1"))
        assertEquals("123", Money.indianGrouping("123"))
        assertEquals("1,234", Money.indianGrouping("1234"))
        assertEquals("12,345", Money.indianGrouping("12345"))
        assertEquals("1,23,456", Money.indianGrouping("123456"))
        assertEquals("12,34,567", Money.indianGrouping("1234567"))
        assertEquals("1,23,45,678", Money.indianGrouping("12345678"))
    }

    @Test
    fun formatQuantityStripsZeros() {
        assertEquals("2", Money.formatQuantity("2.00"))
        assertEquals("2.5", Money.formatQuantity("2.50"))
        assertEquals("2.5", Money.formatQuantity("2.5"))
        assertEquals("1", Money.formatQuantity("1"))
        assertEquals("0.5", Money.formatQuantity("0.5"))
        assertEquals("1.25", Money.formatQuantity("1.25"))
    }
}
