package com.grocery.billing.ui

object Routes {
    const val HOME = "home"
    const val PRODUCTS = "products"
    const val PRODUCT_EDIT = "product_edit?productId={productId}"
    const val PRODUCT_EDIT_ARG = "productId"
    const val CSV_IMPORT = "csv_import"
    const val BILLING = "billing"
    const val BILL_REVIEW = "bill_review"
    const val BILL_COMPLETED = "bill_completed/{billId}"
    const val BILL_COMPLETED_ARG = "billId"
    const val HISTORY = "history"
    const val BILL_DETAIL = "bill_detail/{billId}"
    const val BILL_DETAIL_ARG = "billId"
    const val SETTINGS = "settings"
    const val WAITING_CUSTOMERS = "waiting_customers"
    const val PRICING = "pricing?next={next}"
    const val PRICING_ARG = "next"

    fun productEdit(id: String? = null): String =
        if (id == null) "product_edit" else "product_edit?productId=$id"

    fun billCompleted(billId: Long): String = "bill_completed/$billId"

    fun billDetail(billId: Long): String = "bill_detail/$billId"

    fun pricing(next: String = "billing"): String = "pricing?next=$next"
}
