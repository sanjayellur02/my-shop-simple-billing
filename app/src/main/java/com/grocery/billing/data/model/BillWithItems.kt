package com.grocery.billing.data.model

import com.grocery.billing.data.entity.Bill
import com.grocery.billing.data.entity.BillItem

/** A bill together with its line items. */
data class BillWithItems(
    val bill: Bill,
    val items: List<BillItem>
)
