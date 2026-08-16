package com.grocery.billing.ui.waiting

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grocery.billing.data.dao.HeldBillWithCount
import com.grocery.billing.money.Money
import com.grocery.billing.ui.ViewModelFactory
import com.grocery.billing.ui.Routes
import com.grocery.billing.ui.billing.BillingViewModel
import com.grocery.billing.ui.components.ConfirmDialog
import com.grocery.billing.ui.components.EmptyState
import com.grocery.billing.ui.components.ScreenScaffold
import kotlinx.coroutines.launch

@Composable
fun WaitingCustomersScreen(
    navController: NavHostController,
    factory: ViewModelFactory,
    billingViewModel: BillingViewModel
) {
    val viewModel: WaitingCustomersViewModel = viewModel(factory = factory)
    val heldBills by viewModel.heldBills.collectAsState()
    val scope = rememberCoroutineScope()

    var pendingDelete by remember { mutableStateOf<HeldBillWithCount?>(null) }
    var editingBill by remember { mutableStateOf<HeldBillWithCount?>(null) }

    fun resumeAndNavigate(id: Long, toReview: Boolean) {
        scope.launch {
            val ok = billingViewModel.resumeHeld(id)
            if (ok) {
                val needPricing = billingViewModel.state.value.items.any { it.ratePaise <= 0L }
                val dest = if (toReview) Routes.BILL_REVIEW else Routes.BILLING
                if (needPricing) {
                    navController.navigate(Routes.pricing(if (toReview) "review" else "billing")) {
                        popUpTo(Routes.BILLING) { inclusive = false }
                    }
                } else {
                    navController.navigate(dest) {
                        popUpTo(Routes.BILLING) { inclusive = !toReview }
                    }
                }
            }
        }
    }

    ScreenScaffold(
        title = "Waiting Customers",
        onBack = { navController.popBackStack() }
    ) {
        if (heldBills.isEmpty()) {
            EmptyState(
                "No waiting customers.\nHold a bill to park it here.",
                modifier = Modifier.padding(top = 64.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(heldBills, key = { it.heldBillId }) { held ->
                    HeldBillRow(
                        held = held,
                        onResume = { resumeAndNavigate(held.heldBillId, toReview = false) },
                        onEdit = { editingBill = held },
                        onDelete = { pendingDelete = held },
                        onComplete = { resumeAndNavigate(held.heldBillId, toReview = true) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    pendingDelete?.let { held ->
        ConfirmDialog(
            title = "Delete Waiting Bill",
            message = "Delete the bill for \"${held.reference.ifBlank { "Customer" }}\" (${held.itemCount} item(s), ${Money.paiseToDisplay(held.totalPaise)})?",
            confirmText = "Delete",
            dismissText = "Cancel",
            onConfirm = {
                viewModel.delete(held.heldBillId)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null }
        )
    }

    editingBill?.let { held ->
        EditReferenceDialog(
            initial = held.reference,
            onSave = { ref ->
                viewModel.updateReference(held.heldBillId, ref)
                editingBill = null
            },
            onDismiss = { editingBill = null }
        )
    }
}

@Composable
private fun HeldBillRow(
    held: HeldBillWithCount,
    onResume: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onComplete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Token ${held.billNumber} | ${held.reference.ifBlank { "Customer" }}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "${held.itemCount} item(s) • ${Money.paiseToDisplay(held.totalPaise)} • ${held.billTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "Waiting",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ActionButton("Billing", onResume, Modifier.weight(1f), filled = true)
            ActionButton("Complete", onComplete, Modifier.weight(1f), filled = false)
        }
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ActionButton("Edit", onEdit, Modifier.weight(1f), filled = false)
            ActionButton("Delete", onDelete, Modifier.weight(1f), filled = false, destructive = true)
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    filled: Boolean,
    destructive: Boolean = false
) {
    val color = if (destructive) MaterialTheme.colorScheme.error
    else if (filled) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.secondary
    val contentColor = if (filled) MaterialTheme.colorScheme.onPrimary else color
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
        color = if (filled) color else MaterialTheme.colorScheme.surface,
        border = if (filled) null else androidx.compose.foundation.BorderStroke(1.5.dp, color)
    ) {
        androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
            Text(text, style = MaterialTheme.typography.titleSmall, color = contentColor)
        }
    }
}

@Composable
private fun EditReferenceDialog(
    initial: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var value by remember { mutableStateOf(initial) }
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Customer Reference") },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Name / token / reference") }
            )
        },
        confirmButton = {
            TextButton(onClick = { onSave(value) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
