package com.grocery.billing.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.repository.BillRepository
import com.grocery.billing.data.repository.HeldBillRepository
import com.grocery.billing.data.repository.SettingsRepository
import com.grocery.billing.data.entity.SettingsKeys
import com.grocery.billing.util.Dates
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration

data class HomeUiState(
    val shopName: String = "",
    val todaySalesPaise: Long = 0L,
    val todayBillsCount: Long = 0L,
    val heldCount: Int = 0
)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    billRepository: BillRepository,
    settingsRepository: SettingsRepository,
    heldBillRepository: HeldBillRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    /** Rolls over to the next IST day at midnight so "Today" always matches the current day. */
    private val today = MutableStateFlow(Dates.todayDateString())

    init {
        viewModelScope.launch {
            while (true) {
                val now = Dates.now()
                val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
                delay(Duration.between(now, nextMidnight).toMillis() + 1_000)
                today.value = Dates.todayDateString()
            }
        }
        viewModelScope.launch {
            today.flatMapLatest { date -> billRepository.billsByDate(date) }.collect { bills ->
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
