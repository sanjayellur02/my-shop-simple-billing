package com.grocery.billing.ui.history

import com.grocery.billing.data.entity.Bill
import com.grocery.billing.util.Dates
import java.time.LocalDate

enum class HistoryPeriod(val label: String) {
    ALL("All"),
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month")
}

object BillHistoryFilter {

    fun filter(
        bills: List<Bill>,
        query: String,
        period: HistoryPeriod,
        today: LocalDate
    ): List<Bill> {
        val base = if (query.isBlank()) {
            bills
        } else {
            bills.filter {
                it.billNumber.contains(query, ignoreCase = true) ||
                    it.billDate.contains(query, ignoreCase = true)
            }
        }
        val start = when (period) {
            HistoryPeriod.DAY -> today
            HistoryPeriod.WEEK -> today.minusDays(6)
            HistoryPeriod.MONTH -> today.minusDays(29)
            HistoryPeriod.ALL -> null
        }
        return base.filter { bill ->
            start == null ||
                (Dates.parseDate(bill.billDate)?.let { date -> !date.isBefore(start) } ?: true)
        }.sortedByDescending { it.billId }
    }
}
