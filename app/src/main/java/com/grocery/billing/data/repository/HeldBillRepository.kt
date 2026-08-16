package com.grocery.billing.data.repository

import com.grocery.billing.data.dao.HeldBillDao
import com.grocery.billing.data.dao.HeldBillItemDao
import com.grocery.billing.data.dao.HeldBillWithCount
import com.grocery.billing.data.entity.HeldBill
import com.grocery.billing.data.entity.HeldBillItem
import com.grocery.billing.util.Dates
import kotlinx.coroutines.flow.Flow

data class HeldBillItemDraft(
    val productId: String?,
    val productName: String,
    val quantity: String,
    val ratePaise: Long,
    val amountPaise: Long
)

data class HeldBillWithItems(
    val bill: HeldBill,
    val items: List<HeldBillItem>
)

class HeldBillRepository(
    private val heldBillDao: HeldBillDao,
    private val heldBillItemDao: HeldBillItemDao
) {

    fun observeAll(): Flow<List<HeldBill>> = heldBillDao.observeAll()

    fun observeAllWithCount(): Flow<List<HeldBillWithCount>> = heldBillDao.observeAllWithCount()

    suspend fun getWithItems(id: Long): HeldBillWithItems? {
        val bill = heldBillDao.getById(id) ?: return null
        return HeldBillWithItems(bill, heldBillItemDao.getByHeldBill(id))
    }

    suspend fun hold(
        reference: String,
        billNumber: String,
        date: String,
        time: String,
        items: List<HeldBillItemDraft>,
        subtotalPaise: Long,
        discountPaise: Long,
        totalPaise: Long
    ): Long {
        val id = heldBillDao.insert(
            HeldBill(
                reference = reference,
                billNumber = billNumber,
                billDate = date,
                billTime = time,
                subtotalPaise = subtotalPaise,
                discountPaise = discountPaise,
                totalPaise = totalPaise,
                createdAt = Dates.isoTimestamp()
            )
        )
        heldBillItemDao.insertAll(
            items.map {
                HeldBillItem(
                    heldBillId = id,
                    productId = it.productId,
                    productNameSnapshot = it.productName,
                    quantity = it.quantity,
                    ratePaise = it.ratePaise,
                    amountPaise = it.amountPaise
                )
            }
        )
        return id
    }

    suspend fun delete(id: Long) = heldBillDao.deleteById(id)

    suspend fun updateReference(id: Long, reference: String) =
        heldBillDao.updateReference(id, reference.trim())
}
