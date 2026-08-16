package com.grocery.billing.ui.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.model.BillWithItems
import com.grocery.billing.data.repository.BillRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class BillDetailViewModel(
    billRepository: BillRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val billId = savedStateHandle.get<Long>("billId")

    val billWithItems: StateFlow<BillWithItems?> =
        billId?.let { billRepository.billWithItemsFlow(it) }
            ?.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
            ?: kotlinx.coroutines.flow.MutableStateFlow(null)
}
