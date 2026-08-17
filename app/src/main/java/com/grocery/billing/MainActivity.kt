package com.grocery.billing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grocery.billing.ui.ViewModelFactory
import com.grocery.billing.ui.AppNavHost
import com.grocery.billing.ui.billing.BillingViewModel
import com.grocery.billing.ui.lock.LockScreen
import com.grocery.billing.ui.lock.LockViewModel
import com.grocery.billing.ui.theme.GroceryTheme

class MainActivity : ComponentActivity() {

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory((application as GroceryApp).container)
    }

    private val lockViewModel: LockViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GroceryTheme {
                val locked by lockViewModel.locked.collectAsState()
                if (locked) {
                    LockScreen(lockViewModel)
                } else {
                    GroceryAppRoot(factory)
                }
            }
        }
    }
}

@Composable
private fun GroceryAppRoot(factory: ViewModelFactory) {
    val billingViewModel: BillingViewModel = viewModel(factory = factory)
    AppNavHost(factory = factory, billingViewModel = billingViewModel)
}
