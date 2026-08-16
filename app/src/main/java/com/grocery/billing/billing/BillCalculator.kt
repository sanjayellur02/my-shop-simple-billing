package com.grocery.billing.billing

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Pure billing calculations. All money is in paise (Long).
 * Quantity is a decimal string; the rate is in paise.
 */
object BillCalculator {

    /** amount = quantity * rate, rounded to the nearest paise. */
    fun itemAmountPaise(quantity: String, ratePaise: Long): Long {
        val qty = quantity.trim().toBigDecimalOrNull() ?: return 0L
        return qty.multiply(BigDecimal.valueOf(ratePaise))
            .setScale(0, RoundingMode.HALF_UP)
            .toLong()
    }

    fun subtotalPaise(amountsPaise: List<Long>): Long = amountsPaise.sum()

    /** Grand total cannot go below zero. */
    fun grandTotalPaise(subtotalPaise: Long, discountPaise: Long): Long =
        (subtotalPaise - discountPaise).coerceAtLeast(0L)

    fun isValidQuantity(quantity: String): Boolean {
        val qty = quantity.trim().toBigDecimalOrNull() ?: return false
        return qty > BigDecimal.ZERO
    }

    fun isValidRatePaise(ratePaise: Long): Boolean = ratePaise >= 0L

    fun isValidRateText(rate: String): Boolean {
        val paise = rate.trim().toBigDecimalOrNull() ?: return false
        return paise >= BigDecimal.ZERO
    }
}
