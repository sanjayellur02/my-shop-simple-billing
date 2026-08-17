package com.grocery.billing.data

import android.content.Context
import com.grocery.billing.data.lock.AppLockManager
import com.grocery.billing.data.repository.BillRepository
import com.grocery.billing.data.repository.HeldBillRepository
import com.grocery.billing.data.repository.ProductRepository
import com.grocery.billing.data.repository.SettingsRepository

/** Simple manual dependency container. */
class AppContainer(context: Context) {

    private val database = AppDatabase.getInstance(context)

    val productRepository = ProductRepository(database.productDao(), database.productPriceDao())
    val billRepository = BillRepository(database.billDao(), database.billItemDao())
    val heldBillRepository = HeldBillRepository(database.heldBillDao(), database.heldBillItemDao())
    val settingsRepository = SettingsRepository(database.settingsDao())
    val appLockManager = AppLockManager(context)
}
