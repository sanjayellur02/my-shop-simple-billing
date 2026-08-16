package com.grocery.billing.data.csv

/**
 * Minimal RFC-4180 style CSV parser. Handles quoted fields, commas and
 * escaped quotes inside fields.
 */
object CsvParser {

    fun parse(text: String): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        val row = mutableListOf<String>()
        val field = StringBuilder()
        var inQuotes = false
        var i = 0

        while (i < text.length) {
            val c = text[i]
            when {
                inQuotes -> {
                    when {
                        c == '"' && i + 1 < text.length && text[i + 1] == '"' -> {
                            field.append('"')
                            i++
                        }
                        c == '"' -> inQuotes = false
                        else -> field.append(c)
                    }
                }
                c == '"' -> inQuotes = true
                c == ',' -> {
                    row.add(field.toString())
                    field.setLength(0)
                }
                c == '\r' -> {
                    // ignore; \r\n handled by \n
                }
                c == '\n' -> {
                    row.add(field.toString())
                    field.setLength(0)
                    rows.add(row.toList())
                    row.clear()
                }
                else -> field.append(c)
            }
            i++
        }

        if (field.isNotEmpty() || row.isNotEmpty()) {
            row.add(field.toString())
            rows.add(row.toList())
        }
        return rows
    }
}
