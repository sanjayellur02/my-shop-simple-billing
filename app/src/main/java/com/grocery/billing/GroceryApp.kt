package com.grocery.billing

import android.app.Application
import com.grocery.billing.data.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class GroceryApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        scope.launch { container.settingsRepository.ensureDefaults() }
    }
}
