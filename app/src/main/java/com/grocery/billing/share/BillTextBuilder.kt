package com.grocery.billing.share

import com.grocery.billing.data.model.BillWithItems
import com.grocery.billing.money.Money

/** Builds the plain-text bill message used for WhatsApp / SMS / share. */
object BillTextBuilder {

    fun build(
        shopName: String,
        address: String,
        phone: String,
        showAddress: Boolean,
        billNumber: String,
        date: String,
        time: String,
        items: List<Line>,
        subtotalPaise: Long,
        discountPaise: Long,
        totalPaise: Long,
        thankYou: String
    ): String {
        val sb = StringBuilder()
        sb.appendLine(shopName)
        if (showAddress && address.isNotBlank()) sb.appendLine(address)
        if (phone.isNotBlank()) sb.appendLine("Phone: $phone")
        sb.appendLine()
        sb.appendLine("Bill No: $billNumber")
        sb.appendLine("Date: $date")
        sb.appendLine("Time: $time")
        sb.appendLine()
        items.forEach { line ->
            sb.appendLine(
                "${line.name} — ${line.quantity} × ${Money.paiseToDisplay(line.ratePaise)} = ${Money.paiseToDisplay(line.amountPaise)}"
            )
        }
        sb.appendLine()
        sb.appendLine("Subtotal: ${Money.paiseToDisplay(subtotalPaise)}")
        if (discountPaise > 0) {
            sb.appendLine("Discount: ${Money.paiseToDisplay(discountPaise)}")
        }
        sb.appendLine("Total: ${Money.paiseToDisplay(totalPaise)}")
        sb.appendLine()
        sb.appendLine(thankYou)
        return sb.toString().trim()
    }

    fun buildForBill(
        billWithItems: BillWithItems,
        shopName: String,
        address: String,
        phone: String,
        showAddress: Boolean,
        thankYou: String
    ): String {
        val b = billWithItems.bill
        return build(
            shopName = shopName,
            address = address,
            phone = phone,
            showAddress = showAddress,
            billNumber = b.billNumber,
            date = b.billDate,
            time = b.billTime,
            items = billWithItems.items.map {
                Line(it.productNameSnapshot, it.quantity, it.ratePaise, it.amountPaise)
            },
            subtotalPaise = b.subtotalPaise,
            discountPaise = b.discountPaise,
            totalPaise = b.totalPaise,
            thankYou = thankYou
        )
    }

    data class Line(
        val name: String,
        val quantity: String,
        val ratePaise: Long,
        val amountPaise: Long
    )
}
