package com.grocery.billing.util

/**
 * Generates human-readable SKU codes from product names.
 *
 * Rules:
 * - Single-word products (no trailing number+unit/price): uppercase the whole name.
 *   "Akki" → "AKKI", "Godhi" → "GODHI", "Sakkare" → "SAKKARE"
 *
 * - Multi-word products (no trailing suffix): join words with hyphens, uppercase.
 *   "Togari Bele" → "TOGARI-BELE", "Hesaru Bele" → "HESARU-BELE"
 *
 * - Products with a trailing numeric suffix (e.g. "200g", "5rs", "1L"):
 *   * Brand part is abbreviated if it's a single long word (>5 chars) without a hyphen:
 *     "Colgate 200g" → "COL-200G", "Colgate 100g" → "COL-100G"
 *   * Brand with a hyphen is kept: "Parle-G 5rs" → "PARLE-G-5RS"
 *   * Short brand (≤5 chars): "Maggi 12rs" → "MAGGI-12RS"
 *   * Multi-word brand: "Sunflower Oil 1L" → "SUNFLOWER-OIL-1L"
 */
object SkuGenerator {

    private val SUFFIX_PATTERN = Regex("""^(.*?)\s+(\d+\w*)$""")

    fun generate(productName: String): String {
        val name = productName.trim()
        if (name.isEmpty()) return ""

        val match = SUFFIX_PATTERN.find(name)
        return if (match != null) {
            val brandPart = match.groupValues[1].trim()
            val suffix = match.groupValues[2].trim()
            val brandSku = abbreviateBrand(brandPart)
            "$brandSku-${suffix.uppercase()}"
        } else {
            name.split(Regex("\\s+"))
                .filter { it.isNotEmpty() }
                .joinToString("-") { it.uppercase() }
        }
    }

    private fun abbreviateBrand(brand: String): String {
        val words = brand.split(Regex("\\s+")).filter { it.isNotEmpty() }
        if (words.isEmpty()) return ""
        if (words.size > 1) {
            return words.joinToString("-") { it.uppercase() }
        }
        val word = words[0]
        return if (word.contains("-") || word.length <= 5) {
            word.uppercase()
        } else {
            word.substring(0, 3).uppercase()
        }
    }
}
