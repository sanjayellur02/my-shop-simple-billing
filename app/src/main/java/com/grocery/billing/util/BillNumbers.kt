package com.grocery.billing.util

/** Sequential bill number helpers. Numbers never reset daily. */
object BillNumbers {

    fun format(number: Long): String = number.toString().padStart(6, '0')

    /** Computes the next sequential number from the numbers already stored. */
    fun nextNumber(existingNumbers: List<String>): Long {
        val max = existingNumbers
            .mapNotNull { n -> n.filter { it.isDigit() }.toLongOrNull() }
            .maxOrNull()
        return (max ?: 0L) + 1L
    }
}
