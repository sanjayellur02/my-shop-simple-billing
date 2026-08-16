package com.grocery.billing.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.repository.BillRepository
import com.grocery.billing.data.repository.HeldBillRepository
import com.grocery.billing.data.repository.SettingsRepository
import com.grocery.billing.data.entity.SettingsKeys
import com.grocery.billing.util.Dates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val shopName: String = "",
    val todaySalesPaise: Long = 0L,
    val todayBillsCount: Long = 0L,
    val heldCount: Int = 0
)

class HomeViewModel(
    billRepository: BillRepository,
    settingsRepository: SettingsRepository,
    heldBillRepository: HeldBillRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        val today = Dates.todayDateString()
        viewModelScope.launch {
            billRepository.billsByDate(today).collect { bills ->
                _state.update {
                    it.copy(
                        todaySalesPaise = bills.sumOf { bill -> bill.totalPaise },
                        todayBillsCount = bills.size.toLong()
                    )
                }
            }
        }
        viewModelScope.launch {
            settingsRepository.observe(SettingsKeys.SHOP_NAME).collect { name ->
                _state.update { it.copy(shopName = name) }
            }
        }
        viewModelScope.launch {
            heldBillRepository.observeAll().collect { held ->
                _state.update { it.copy(heldCount = held.size) }
            }
        }
    }
}
