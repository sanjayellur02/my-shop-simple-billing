package com.grocery.billing.share

import com.grocery.billing.print.BillLine

data class ShareBillData(
    val shopName: String,
    val address: String,
    val phone: String,
    val showAddress: Boolean,
    val billNumber: String,
    val date: String,
    val time: String,
    val items: List<BillLine>,
    val subtotalPaise: Long,
    val discountPaise: Long,
    val totalPaise: Long,
    val thankYou: String,
    val text: String
)
