package com.grocery.billing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.grocery.billing.data.AppContainer
import com.grocery.billing.ui.billing.BillingViewModel
import com.grocery.billing.ui.history.BillDetailViewModel
import com.grocery.billing.ui.history.BillHistoryViewModel
import com.grocery.billing.ui.home.HomeViewModel
import com.grocery.billing.ui.lock.LockViewModel
import com.grocery.billing.ui.products.CsvImportViewModel
import com.grocery.billing.ui.products.ProductEditViewModel
import com.grocery.billing.ui.products.ProductListViewModel
import com.grocery.billing.ui.settings.SettingsViewModel
import com.grocery.billing.ui.waiting.WaitingCustomersViewModel

class ViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val handle = extras.createSavedStateHandle()
        val result: ViewModel = when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(
                    container.billRepository,
                    container.settingsRepository,
                    container.heldBillRepository
                )
            modelClass.isAssignableFrom(ProductListViewModel::class.java) ->
                ProductListViewModel(container.productRepository)
            modelClass.isAssignableFrom(ProductEditViewModel::class.java) ->
                ProductEditViewModel(container.productRepository, container.draftRepository, handle)
            modelClass.isAssignableFrom(CsvImportViewModel::class.java) ->
                CsvImportViewModel(container.productRepository)
            modelClass.isAssignableFrom(BillingViewModel::class.java) ->
                BillingViewModel(
                    container.productRepository,
                    container.billRepository,
                    container.heldBillRepository,
                    container.settingsRepository,
                    container.draftRepository
                )
            modelClass.isAssignableFrom(WaitingCustomersViewModel::class.java) ->
                WaitingCustomersViewModel(container.heldBillRepository)
            modelClass.isAssignableFrom(BillHistoryViewModel::class.java) ->
                BillHistoryViewModel(container.billRepository)
            modelClass.isAssignableFrom(BillDetailViewModel::class.java) ->
                BillDetailViewModel(container.billRepository, handle)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(
                    container.settingsRepository,
                    container.appLockManager
                )
            modelClass.isAssignableFrom(LockViewModel::class.java) ->
                LockViewModel(container.appLockManager)
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
        @Suppress("UNCHECKED_CAST")
        return result as T
    }
}
