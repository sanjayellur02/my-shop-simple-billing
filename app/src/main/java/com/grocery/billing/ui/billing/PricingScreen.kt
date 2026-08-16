package com.grocery.billing.ui.billing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.grocery.billing.money.Money
import com.grocery.billing.ui.Routes
import com.grocery.billing.ui.components.EmptyState
import com.grocery.billing.ui.components.NumberField
import com.grocery.billing.ui.components.ScreenScaffold
import kotlinx.coroutines.launch

/**
 * Lists every item in the current draft bill with a price input box beside
 * its name and quantity. Pressing Done on the keyboard jumps to the next
 * item (like Tab on a PC), scrolling the list as it goes. Typed prices
 * calculate the amount for each item instantly.
 */
@Composable
fun PricingScreen(
    navController: NavHostController,
    billingViewModel: BillingViewModel,
    next: String
) {
    val state by billingViewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current

    // Persist per-item text across recompositions (keyed by item key).
    val priceTexts = remember { mutableStateMapOf<Long, String>() }
    val focusers = remember { mutableMapOf<Long, FocusRequester>() }
    val itemKeys = state.items.map { it.key }

    LaunchedEffect(itemKeys) {
        for (item in state.items) {
            if (!priceTexts.containsKey(item.key)) {
                priceTexts[item.key] =
                    if (item.ratePaise > 0L) Money.paiseToNumber(item.ratePaise) else ""
            }
        }
    }

    var showUnpricedConfirm by remember { mutableStateOf(false) }

    fun navigateAway() {
        navController.popBackStack()
        val dest = if (next == "review") Routes.BILL_REVIEW else Routes.BILLING
        navController.navigate(dest) { launchSingleTop = true }
    }

    fun onPriceChange(item: DraftItem, value: String) {
        priceTexts[item.key] = value
        val rate = Money.parseRupeesToPaise(value) ?: 0L
        billingViewModel.updateItem(item.key, item.quantity, rate)
    }

    fun advanceFrom(index: Int) {
        val nextIndex = index + 1
        if (nextIndex < state.items.size) {
            val nextItem = state.items[nextIndex]
            scope.launch {
                listState.animateScrollToItem(nextIndex)
                focusers[nextItem.key]?.requestFocus()
            }
        } else {
            keyboard?.hide()
        }
    }

    fun finish() {
        if (state.items.any { it.ratePaise <= 0L }) {
            showUnpricedConfirm = true
        } else {
            navigateAway()
        }
    }

    ScreenScaffold(title = "Set Prices", onBack = { navController.popBackStack() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .imePadding()
        ) {
            Text(
                "Type each item's price and press Done to jump to the next item.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))

            if (state.items.isEmpty()) {
                EmptyState("No items to price.")
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(state.items, key = { _, item -> item.key }) { index, item ->
                        PricingRow(
                            item = item,
                            priceText = priceTexts[item.key] ?: "",
                            onPriceChange = { onPriceChange(item, it) },
                            onDone = { advanceFrom(index) },
                            focusRequester = focusers.getOrPut(item.key) { FocusRequester() }
                        )
                        HorizontalDivider()
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Subtotal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            Money.paiseToDisplay(state.subtotalPaise),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = { finish() },
                        modifier = Modifier.height(52.dp)
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }

    if (showUnpricedConfirm) {
        val missing = state.items.count { it.ratePaise <= 0L }
        AlertDialog(
            onDismissRequest = { showUnpricedConfirm = false },
            title = { Text("Prices still missing") },
            text = { Text("$missing item(s) have no price yet. Continue without pricing them?") },
            confirmButton = {
                TextButton(onClick = {
                    showUnpricedConfirm = false
                    navigateAway()
                }) {
                    Text("Continue")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnpricedConfirm = false }) {
                    Text("Keep Editing")
                }
            }
        )
    }
}

@Composable
private fun PricingRow(
    item: DraftItem,
    priceText: String,
    onPriceChange: (String) -> Unit,
    onDone: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.productName, style = MaterialTheme.typography.titleSmall)
                Text(
                    "Qty: ${item.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                Money.paiseToDisplay(item.amountPaise),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(6.dp))
        NumberField(
            value = priceText,
            onValueChange = onPriceChange,
            label = "Price ₹",
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            onDone = onDone
        )
    }
}
