package com.grocery.billing.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.lock.AppLockManager
import com.grocery.billing.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val shopName: String = "",
    val address: String = "",
    val phone: String = "",
    val currencySymbol: String = "₹",
    val thankYou: String = "",
    val customerThankYou: String = "",
    val showShopAddress: Boolean = true,
    val allowPriceOverride: Boolean = true,
    val lockEnabled: Boolean = false,
    val fingerprintEnabled: Boolean = false,
    val saved: Boolean = false
)

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val lockManager: AppLockManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.observeShopSettings().collect { s ->
                _state.value = SettingsUiState(
                    shopName = s.shopName,
                    address = s.address,
                    phone = s.phone,
                    currencySymbol = s.currencySymbol,
                    thankYou = s.thankYou,
                    customerThankYou = s.customerThankYou,
                    showShopAddress = s.showShopAddress,
                    allowPriceOverride = s.allowPriceOverride,
                    lockEnabled = lockManager.isPinSet(),
                    fingerprintEnabled = lockManager.isFingerprintEnabled()
                )
            }
        }
    }

    fun onShopNameChange(v: String) = _state.update { it.copy(shopName = v, saved = false) }
    fun onAddressChange(v: String) = _state.update { it.copy(address = v, saved = false) }
    fun onPhoneChange(v: String) = _state.update { it.copy(phone = v, saved = false) }
    fun onCurrencyChange(v: String) = _state.update { it.copy(currencySymbol = v, saved = false) }
    fun onThankYouChange(v: String) = _state.update { it.copy(thankYou = v, saved = false) }
    fun onCustomerThankYouChange(v: String) = _state.update { it.copy(customerThankYou = v, saved = false) }
    fun onShowAddressChange(v: Boolean) = _state.update { it.copy(showShopAddress = v, saved = false) }
    fun onAllowPriceOverrideChange(v: Boolean) = _state.update { it.copy(allowPriceOverride = v, saved = false) }

    fun save() {
        val s = _state.value
        viewModelScope.launch {
            settingsRepository.setAll(
                mapOf(
                    com.grocery.billing.data.entity.SettingsKeys.SHOP_NAME to s.shopName,
                    com.grocery.billing.data.entity.SettingsKeys.SHOP_ADDRESS to s.address,
                    com.grocery.billing.data.entity.SettingsKeys.SHOP_PHONE to s.phone,
                    com.grocery.billing.data.entity.SettingsKeys.CURRENCY_SYMBOL to s.currencySymbol,
                    com.grocery.billing.data.entity.SettingsKeys.THANK_YOU_MESSAGE to s.thankYou,
                    com.grocery.billing.data.entity.SettingsKeys.CUSTOMER_THANK_YOU to s.customerThankYou,
                    com.grocery.billing.data.entity.SettingsKeys.SHOW_SHOP_ADDRESS to s.showShopAddress.toString(),
                    com.grocery.billing.data.entity.SettingsKeys.ALLOW_PRICE_OVERRIDE to s.allowPriceOverride.toString()
                )
            )
            _state.update { it.copy(saved = true) }
        }
    }

    fun clearSaved() {
        _state.update { it.copy(saved = false) }
    }

    fun setLockPin(pin: String) {
        lockManager.setPin(pin)
        _state.update { it.copy(lockEnabled = true, fingerprintEnabled = false, saved = false) }
    }

    /** Returns true when the PIN was correct and the lock was turned off. */
    fun disableLock(currentPin: String): Boolean {
        if (!lockManager.verifyPin(currentPin)) return false
        lockManager.disable()
        _state.update { it.copy(lockEnabled = false, fingerprintEnabled = false, saved = false) }
        return true
    }

    fun setFingerprintEnabled(enabled: Boolean) {
        lockManager.setFingerprintEnabled(enabled)
        _state.update { it.copy(fingerprintEnabled = enabled, saved = false) }
    }
}
