package com.grocery.billing.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.grocery.billing.GroceryApp
import com.grocery.billing.data.repository.ShopSettings

/** Observes the shop settings from Room. Reusable across review/detail screens. */
@Composable
fun rememberShopSettings(): ShopSettings? {
    val context = LocalContext.current
    val repository = remember {
        (context.applicationContext as GroceryApp).container.settingsRepository
    }
    val settings by repository.observeShopSettings().collectAsState(initial = null)
    return settings
}
