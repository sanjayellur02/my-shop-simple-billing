package com.grocery.billing.ui.billing

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.grocery.billing.GroceryApp
import com.grocery.billing.data.entity.Bill
import com.grocery.billing.money.Money
import com.grocery.billing.ui.Routes
import com.grocery.billing.ui.components.LargeButton

@Composable
fun BillCompletedScreen(
    navController: NavHostController,
    billId: Long?
) {
    val context = LocalContext.current
    val repository = remember {
        (context.applicationContext as GroceryApp).container.billRepository
    }
    val bill by produceState<Bill?>(null, billId) {
        value = billId?.let { repository.getById(it) }
    }

    BackHandler {
        navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.height(96.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Bill Completed", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "Bill No: ${bill?.billNumber ?: "—"}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))
            Text("Total", style = MaterialTheme.typography.bodyLarge)
            Text(
                Money.paiseToDisplay(bill?.totalPaise ?: 0L),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(32.dp))
            LargeButton(
                text = "NEW BILL",
                onClick = {
                    navController.navigate(Routes.BILLING) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                height = 64.dp
            )
            Spacer(Modifier.height(12.dp))
            LargeButton(
                text = "VIEW BILL",
                onClick = {
                    billId?.let {
                        navController.navigate(Routes.billDetail(it)) {
                            popUpTo(Routes.HOME) { inclusive = false }
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = androidx.compose.ui.graphics.Color.White,
                height = 56.dp
            )
        }
    }
}
