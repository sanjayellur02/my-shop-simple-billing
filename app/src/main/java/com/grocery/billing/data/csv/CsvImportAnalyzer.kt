package com.grocery.billing.data.csv

import com.grocery.billing.money.Money

data class ProductImportRow(
    val id: String,
    val name: String,
    val sellingPricePaise: Long = 0L,
    val unit: String = "",
    val barcode: String? = null,
    val extraOptions: List<ProductExtraOption> = emptyList()
)

data class ProductExtraOption(
    val sellingPricePaise: Long,
    val unit: String
)

data class CsvImportAnalysis(
    val totalRows: Int,
    val valid: List<ProductImportRow>,
    val duplicateCount: Int,
    val invalidCount: Int,
    val errors: List<String>,
    val headerError: String? = null
) {
    val isReady: Boolean get() = headerError == null && totalRows > 0
}

object CsvImportAnalyzer {

    const val COL_ID = "id"
    const val COL_NAME = "product_name"
    const val COL_PRICE = "price"
    const val COL_UNIT = "unit"
    const val COL_BARCODE = "barcode"

    fun analyze(rows: List<List<String>>, existingIds: Set<String>): CsvImportAnalysis {
        if (rows.isEmpty()) {
            return CsvImportAnalysis(0, emptyList(), 0, 0, emptyList(), "The file is empty.")
        }

        val header = rows[0].map { it.trim().lowercase() }
        val idIdx = header.indexOf(COL_ID)
        val nameIdx = header.indexOf(COL_NAME)
        if (idIdx < 0 || nameIdx < 0) {
            return CsvImportAnalysis(
                0, emptyList(), 0, 0, emptyList(),
                "Columns must include \"id\" and \"product_name\"."
            )
        }
        val priceIdx = header.indexOf(COL_PRICE)
        val unitIdx = header.indexOf(COL_UNIT)
        val barcodeIdx = header.indexOf(COL_BARCODE)

        val dataRows = rows.drop(1).filter { r -> r.any { it.isNotBlank() } }
        val byId = LinkedHashMap<String, ProductImportRow>()
        val errors = mutableListOf<String>()
        var duplicates = 0
        var invalid = 0

        for (r in dataRows) {
            val id = r.getOrNull(idIdx)?.trim() ?: ""
            val name = r.getOrNull(nameIdx)?.trim() ?: ""
            val priceText = priceIdx?.let { r.getOrNull(it)?.trim() ?: "" } ?: ""
            val unit = unitIdx?.let { r.getOrNull(it)?.trim() ?: "" } ?: ""
            val barcode = barcodeIdx?.let { r.getOrNull(it)?.trim() } ?: ""

            var pricePaise = 0L
            if (priceText.isNotEmpty()) {
                val parsed = Money.parseRupeesToPaise(priceText)
                if (parsed == null || parsed < 0) {
                    invalid++
                    if (errors.size < 10) errors.add("Row $id: invalid price \"$priceText\".")
                    continue
                }
                pricePaise = parsed
            }

            if (id.isEmpty()) {
                invalid++
                if (errors.size < 10) errors.add("Row with empty product id.")
                continue
            }

            if (id in existingIds) {
                duplicates++
                if (errors.size < 10) errors.add("Row $id: duplicate product id.")
                continue
            }

            val existing = byId[id]
            if (existing != null) {
                if (priceText.isNotEmpty()) {
                    byId[id] = existing.copy(
                        extraOptions = existing.extraOptions + ProductExtraOption(pricePaise, unit)
                    )
                } else {
                    duplicates++
                    if (errors.size < 10) errors.add("Row $id: duplicate product id.")
                }
                continue
            }

            if (name.isEmpty()) {
                invalid++
                if (errors.size < 10) errors.add("Row $id: empty product name.")
                continue
            }

            byId[id] = ProductImportRow(
                id = id,
                name = name,
                sellingPricePaise = pricePaise,
                unit = unit,
                barcode = barcode.ifEmpty { null }
            )
        }

        return CsvImportAnalysis(dataRows.size, byId.values.toList(), duplicates, invalid, errors)
    }
}
