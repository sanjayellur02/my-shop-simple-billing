package com.grocery.billing.ui.lock

import androidx.lifecycle.ViewModel
import com.grocery.billing.data.lock.AppLockManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LockViewModel(
    private val lockManager: AppLockManager
) : ViewModel() {

    private val _locked = MutableStateFlow(lockManager.isPinSet())
    val locked: StateFlow<Boolean> = _locked.asStateFlow()

    val fingerprintEnabled: Boolean = lockManager.isFingerprintEnabled()

    fun unlockWithPin(pin: String): Boolean {
        val ok = lockManager.verifyPin(pin)
        if (ok) _locked.value = false
        return ok
    }

    fun unlock() {
        _locked.value = false
    }
}
