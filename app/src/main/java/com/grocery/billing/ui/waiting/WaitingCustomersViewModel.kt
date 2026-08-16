package com.grocery.billing.ui.waiting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.dao.HeldBillWithCount
import com.grocery.billing.data.repository.HeldBillRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WaitingCustomersViewModel(
    private val heldBillRepository: HeldBillRepository
) : ViewModel() {

    val heldBills: StateFlow<List<HeldBillWithCount>> = heldBillRepository
        .observeAllWithCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun delete(id: Long) {
        viewModelScope.launch { heldBillRepository.delete(id) }
    }

    fun updateReference(id: Long, reference: String) {
        viewModelScope.launch { heldBillRepository.updateReference(id, reference) }
    }
}
