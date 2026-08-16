package com.grocery.billing.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.entity.Bill
import com.grocery.billing.data.repository.BillRepository
import com.grocery.billing.util.Dates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HistoryUiState(
    val bills: List<Bill> = emptyList(),
    val billCount: Int = 0,
    val totalSalesPaise: Long = 0L
)

class BillHistoryViewModel(
    private val billRepository: BillRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _period = MutableStateFlow(HistoryPeriod.ALL)
    val period: StateFlow<HistoryPeriod> = _period.asStateFlow()

    val state: StateFlow<HistoryUiState> = combine(
        _query,
        _period,
        billRepository.bills
    ) { q, p, all ->
        val today = Dates.parseDate(Dates.todayDateString())
            ?: return@combine HistoryUiState()
        val filtered = BillHistoryFilter.filter(all, q.trim(), p, today)
        HistoryUiState(
            bills = filtered,
            billCount = filtered.size,
            totalSalesPaise = filtered.sumOf { it.totalPaise }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryUiState())

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun onPeriodChange(value: HistoryPeriod) {
        _period.value = value
    }
}
