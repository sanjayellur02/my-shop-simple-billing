package com.grocery.billing.money

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * All money values in the app are stored as integer paise (1 rupee = 100 paise)
 * to avoid floating point currency errors.
 */
object Money {

    const val PAISE_PER_RUPEE = 100L

    /** Parses a user-typed rupee amount ("50.50", "1,000") into paise. Returns null if invalid. */
    fun parseRupeesToPaise(input: String): Long? {
        val cleaned = input.trim().replace("₹", "").replace("Rs", "").replace(",", "").replace(" ", "")
        if (cleaned.isEmpty()) return null
        val bd = cleaned.toBigDecimalOrNull() ?: return null
        return try {
            bd.multiply(BigDecimal(PAISE_PER_RUPEE))
                .setScale(0, RoundingMode.HALF_UP)
                .toLong()
        } catch (e: ArithmeticException) {
            return null
        }
    }

    /** Formats paise as "₹12,450" / "₹50.50". Trailing ".00" is hidden. */
    fun paiseToDisplay(paise: Long): String {
        val sign = if (paise < 0) "-" else ""
        val abs = kotlin.math.abs(paise)
        val rupees = abs / PAISE_PER_RUPEE
        val p = abs % PAISE_PER_RUPEE
        return "$sign₹${indianGrouping(rupees.toString())}" +
            if (p != 0L) "." + p.toString().padStart(2, '0') else ""
    }

    /** Formats paise as "12,450" / "50.50" (no currency symbol). */
    fun paiseToNumber(paise: Long): String {
        val sign = if (paise < 0) "-" else ""
        val abs = kotlin.math.abs(paise)
        val rupees = abs / PAISE_PER_RUPEE
        val p = abs % PAISE_PER_RUPEE
        return "$sign${indianGrouping(rupees.toString())}" +
            if (p != 0L) "." + p.toString().padStart(2, '0') else ""
    }

    /** Applies Indian digit grouping (12,34,567) to a digit-only string. */
    fun indianGrouping(digits: String): String {
        if (digits.isEmpty()) return digits
        if (digits.length <= 3) return digits
        val last3 = digits.takeLast(3)
        var rest = digits.dropLast(3)
        val parts = mutableListOf(last3)
        while (rest.length > 2) {
            parts.add(0, rest.takeLast(2))
            rest = rest.dropLast(2)
        }
        if (rest.isNotEmpty()) parts.add(0, rest)
        return parts.joinToString(",")
    }

    /** Normalizes a user-typed quantity ("2.50" -> "2.5", "2.00" -> "2"). */
    fun formatQuantity(raw: String): String {
        val cleaned = raw.trim()
        if (cleaned.isEmpty()) return cleaned
        val bd = cleaned.toBigDecimalOrNull() ?: return cleaned
        return bd.stripTrailingZeros().toPlainString()
    }
}
