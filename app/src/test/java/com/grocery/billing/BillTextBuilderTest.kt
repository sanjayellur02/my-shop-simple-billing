package com.grocery.billing

import com.grocery.billing.share.BillTextBuilder
import org.junit.Assert.assertTrue
import org.junit.Test

class BillTextBuilderTest {

    @Test
    fun buildsFullBillText() {
        val text = BillTextBuilder.build(
            shopName = "ABC Grocery Store",
            address = "Main Road",
            phone = "9876543210",
            showAddress = true,
            billNumber = "000123",
            date = "16/08/2026",
            time = "9:42 PM",
            items = listOf(
                BillTextBuilder.Line("Rice", "2", 6000, 12000),
                BillTextBuilder.Line("Sugar", "1", 4500, 4500)
            ),
            subtotalPaise = 16500,
            discountPaise = 2500,
            totalPaise = 14000,
            thankYou = "Thank you for shopping with us!"
        )

        assertTrue(text.contains("ABC Grocery Store"))
        assertTrue(text.contains("Main Road"))
        assertTrue(text.contains("Phone: 9876543210"))
        assertTrue(text.contains("Bill No: 000123"))
        assertTrue(text.contains("Date: 16/08/2026"))
        assertTrue(text.contains("Time: 9:42 PM"))
        assertTrue(text.contains("Rice — 2 × ₹60 = ₹120"))
        assertTrue(text.contains("Sugar — 1 × ₹45 = ₹45"))
        assertTrue(text.contains("Subtotal: ₹165"))
        assertTrue(text.contains("Discount: ₹25"))
        assertTrue(text.contains("Total: ₹140"))
        assertTrue(text.contains("Thank you for shopping with us!"))
    }

    @Test
    fun omitsDiscountWhenZero() {
        val text = BillTextBuilder.build(
            shopName = "S",
            address = "",
            phone = "",
            showAddress = true,
            billNumber = "1",
            date = "d",
            time = "t",
            items = emptyList(),
            subtotalPaise = 100,
            discountPaise = 0,
            totalPaise = 100,
            thankYou = "Thanks"
        )
        assertTrue(!text.contains("Discount"))
    }

    @Test
    fun omitsAddressWhenHidden() {
        val text = BillTextBuilder.build(
            shopName = "S",
            address = "Secret Address",
            phone = "",
            showAddress = false,
            billNumber = "1",
            date = "d",
            time = "t",
            items = emptyList(),
            subtotalPaise = 0,
            discountPaise = 0,
            totalPaise = 0,
            thankYou = "Thanks"
        )
        assertTrue(!text.contains("Secret Address"))
    }
}
