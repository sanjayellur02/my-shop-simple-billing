package com.grocery.billing

import com.grocery.billing.data.entity.Bill
import com.grocery.billing.ui.history.BillHistoryFilter
import com.grocery.billing.ui.history.HistoryPeriod
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BillHistoryFilterTest {

    private val fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    private fun bill(id: Long, date: String, number: String = "%06d".format(id), total: Long = 100L) =
        Bill(
            billId = id,
            billNumber = number,
            billDate = date,
            billTime = "1:00 PM",
            subtotalPaise = total,
            discountPaise = 0L,
            totalPaise = total,
            createdAt = ""
        )

    private fun d(date: LocalDate): String = date.format(fmt)

    @Test
    fun dayShowsOnlyToday() {
        val today = LocalDate.of(2026, 8, 16)
        val bills = listOf(
            bill(1, d(today)),
            bill(2, d(today.minusDays(3))),
            bill(3, d(today.minusDays(10)))
        )
        val result = BillHistoryFilter.filter(bills, "", HistoryPeriod.DAY, today)
        assertEquals(listOf(1L), result.map { it.billId })
    }

    @Test
    fun weekShowsLastSevenDays() {
        val today = LocalDate.of(2026, 8, 16)
        val bills = listOf(
            bill(1, d(today)),
            bill(2, d(today.minusDays(6))),
            bill(3, d(today.minusDays(7))),
            bill(4, d(today.minusDays(30)))
        )
        val result = BillHistoryFilter.filter(bills, "", HistoryPeriod.WEEK, today)
        assertEquals(listOf(1L, 2L), result.map { it.billId })
    }

    @Test
    fun monthShowsLastThirtyDays() {
        val today = LocalDate.of(2026, 8, 16)
        val bills = listOf(
            bill(1, d(today)),
            bill(2, d(today.minusDays(29))),
            bill(3, d(today.minusDays(30))),
            bill(4, d(today.minusDays(60)))
        )
        val result = BillHistoryFilter.filter(bills, "", HistoryPeriod.MONTH, today)
        assertEquals(listOf(1L, 2L), result.map { it.billId })
    }

    @Test
    fun allShowsEverything() {
        val today = LocalDate.of(2026, 8, 16)
        val bills = listOf(
            bill(1, d(today)),
            bill(2, d(today.minusDays(300)))
        )
        val result = BillHistoryFilter.filter(bills, "", HistoryPeriod.ALL, today)
        assertEquals(listOf(1L, 2L), result.map { it.billId })
    }

    @Test
    fun queryFiltersByBillNumber() {
        val today = LocalDate.of(2026, 8, 16)
        val bills = listOf(
            bill(1, d(today), number = "000123"),
            bill(2, d(today), number = "000456")
        )
        val result = BillHistoryFilter.filter(bills, "123", HistoryPeriod.ALL, today)
        assertEquals(listOf(1L), result.map { it.billId })
    }

    @Test
    fun queryAndPeriodCombine() {
        val today = LocalDate.of(2026, 8, 16)
        val bills = listOf(
            bill(1, d(today), number = "000111"),
            bill(2, d(today.minusDays(2)), number = "000111"),
            bill(3, d(today.minusDays(20)), number = "000111")
        )
        val result = BillHistoryFilter.filter(bills, "111", HistoryPeriod.WEEK, today)
        assertEquals(listOf(1L, 2L), result.map { it.billId })
    }

    @Test
    fun resultsSortedByBillIdDesc() {
        val today = LocalDate.of(2026, 8, 16)
        val bills = listOf(
            bill(1, d(today)),
            bill(2, d(today)),
            bill(3, d(today))
        )
        val result = BillHistoryFilter.filter(bills, "", HistoryPeriod.DAY, today)
        assertEquals(listOf(3L, 2L, 1L), result.map { it.billId })
    }
}
