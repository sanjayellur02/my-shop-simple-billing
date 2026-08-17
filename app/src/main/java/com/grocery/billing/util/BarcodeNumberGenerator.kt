package com.grocery.billing.util

import kotlin.random.Random

/**
 * Generates unique 8-digit numeric barcodes.
 *
 * - Exactly 8 digits, numbers only.
 * - Checks a set of existing barcodes to avoid duplicates.
 * - For batch operations, accumulates generated barcodes in a local set
 *   to avoid within-batch collisions without repeated database queries.
 */
object BarcodeNumberGenerator {

    private const val BARCODE_DIGITS = 8
    private const val MIN_VALUE = 10_000_000
    private const val MAX_VALUE = 99_999_999
    private const val MAX_ATTEMPTS = 50

    fun generate(existingBarcodes: Set<String>): String {
        repeat(MAX_ATTEMPTS) {
            val candidate = (MIN_VALUE..MAX_VALUE).random().toString()
            if (candidate !in existingBarcodes) return candidate
        }
        return System.nanoTime().toString().takeLast(BARCODE_DIGITS).padStart(BARCODE_DIGITS, '0')
    }

    fun generateBatch(count: Int, existingBarcodes: Set<String>): List<String> {
        val taken = existingBarcodes.toMutableSet()
        val result = mutableListOf<String>()
        repeat(count) {
            val code = generate(taken)
            taken += code
            result += code
        }
        return result
    }
}
