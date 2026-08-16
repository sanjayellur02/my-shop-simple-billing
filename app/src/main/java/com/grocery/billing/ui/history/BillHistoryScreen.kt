package com.grocery.billing.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grocery.billing.data.entity.Bill
import com.grocery.billing.money.Money
import com.grocery.billing.ui.ViewModelFactory
import com.grocery.billing.ui.Routes
import com.grocery.billing.ui.components.EmptyState
import com.grocery.billing.ui.components.ScreenScaffold
import com.grocery.billing.util.Dates

@Composable
fun BillHistoryScreen(navController: NavHostController, factory: ViewModelFactory) {
    val viewModel: BillHistoryViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()
    val query by viewModel.query.collectAsState()
    val period by viewModel.period.collectAsState()

    ScreenScaffold(title = "Bill History", onBack = { navController.popBackStack() }) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Search by Bill Number or Date") }
            )
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HistoryPeriod.values().forEach { p ->
                    FilterChip(
                        selected = period == p,
                        onClick = { viewModel.onPeriodChange(p) },
                        label = { Text(p.label) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            HistoryTotalsCard(state)
            Spacer(Modifier.height(8.dp))

            if (state.bills.isEmpty()) {
                EmptyState(
                    if (query.isBlank() && period == HistoryPeriod.ALL) "No bills yet."
                    else "No bills match this period."
                )
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.bills, key = { it.billId }) { bill ->
                        BillHistoryRow(
                            bill = bill,
                            onClick = { navController.navigate(Routes.billDetail(bill.billId)) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryTotalsCard(state: HistoryUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${state.billCount} bill(s)",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    Money.paiseToDisplay(state.totalSalesPaise),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun BillHistoryRow(bill: Bill, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Bill #${bill.billNumber}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "${Dates.shortDate(bill.billDate)} • ${bill.billTime}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            Money.paiseToDisplay(bill.totalPaise),
            style = MaterialTheme.typography.titleLarge
        )
    }
}
