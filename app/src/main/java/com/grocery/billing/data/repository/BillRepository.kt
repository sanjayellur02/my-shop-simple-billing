package com.grocery.billing.data.repository

import com.grocery.billing.data.dao.BillDao
import com.grocery.billing.data.dao.BillItemDao
import com.grocery.billing.data.entity.Bill
import com.grocery.billing.data.entity.BillItem
import com.grocery.billing.data.model.BillWithItems
import com.grocery.billing.util.BillNumbers
import com.grocery.billing.util.Dates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class BillRepository(
    private val billDao: BillDao,
    private val billItemDao: BillItemDao
) {

    val bills: Flow<List<Bill>> = billDao.observeAll()

    fun billsByDate(date: String): Flow<List<Bill>> = billDao.observeByDate(date)

    fun search(q: String): Flow<List<Bill>> = billDao.search(q)

    suspend fun getById(billId: Long): Bill? = billDao.getById(billId)

    fun itemsForBill(billId: Long): Flow<List<BillItem>> = billItemDao.observeByBill(billId)

    fun billWithItemsFlow(billId: Long): Flow<BillWithItems?> =
        combine(billDao.observeById(billId), billItemDao.observeByBill(billId)) { bill, items ->
            bill?.let { BillWithItems(it, items) }
        }

    suspend fun getBillWithItems(billId: Long): BillWithItems? {
        val bill = billDao.getById(billId) ?: return null
        return BillWithItems(bill, billItemDao.getByBill(billId))
    }

    suspend fun nextBillNumber(): Long =
        BillNumbers.nextNumber(billDao.allBillNumbers())

    /** Persists a completed bill. Returns the new bill id. */
    suspend fun saveBill(
        billNumber: String,
        date: String = Dates.todayDateString(),
        time: String = Dates.timeString(),
        items: List<BillItemDraft>,
        subtotalPaise: Long,
        discountPaise: Long,
        totalPaise: Long
    ): Long {
        val billId = billDao.insert(
            Bill(
                billNumber = billNumber,
                billDate = date,
                billTime = time,
                subtotalPaise = subtotalPaise,
                discountPaise = discountPaise,
                totalPaise = totalPaise,
                createdAt = Dates.isoTimestamp()
            )
        )
        billItemDao.insertAll(
            items.map {
                BillItem(
                    billId = billId,
                    productId = it.productId,
                    productNameSnapshot = it.productName,
                    quantity = it.quantity,
                    ratePaise = it.ratePaise,
                    amountPaise = it.amountPaise
                )
            }
        )
        return billId
    }
}

data class BillItemDraft(
    val productId: String?,
    val productName: String,
    val quantity: String,
    val ratePaise: Long,
    val amountPaise: Long
)
