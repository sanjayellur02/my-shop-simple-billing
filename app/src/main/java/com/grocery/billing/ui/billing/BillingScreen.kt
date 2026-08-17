package com.grocery.billing.ui.billing

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.grocery.billing.data.entity.Product
import com.grocery.billing.money.Money
import com.grocery.billing.ui.Routes
import com.grocery.billing.ui.ViewModelFactory
import com.grocery.billing.ui.components.ConfirmDialog
import com.grocery.billing.ui.components.ErrorText
import com.grocery.billing.ui.components.NumberField
import com.grocery.billing.ui.components.ScreenScaffold
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun BillingScreen(
    navController: NavHostController,
    factory: ViewModelFactory,
    billingViewModel: BillingViewModel
) {
    val state by billingViewModel.state.collectAsState()
    val searchFocus = remember { FocusRequester() }
    val qtyFocus = remember { FocusRequester() }
    val rateFocus = remember { FocusRequester() }

    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        result.contents?.let { billingViewModel.onBarcodeScanned(it) }
    }

    var removingItem by remember { mutableStateOf<DraftItem?>(null) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    var showHoldDialog by remember { mutableStateOf(false) }
    var showNewBillDialog by remember { mutableStateOf(false) }

    val keyboard = LocalSoftwareKeyboardController.current

    val pickerActive = state.selectedProduct != null || state.searchQuery.isNotBlank()

    LaunchedEffect(Unit) {
        billingViewModel.ensureStarted()
    }

    LaunchedEffect(state.draftRestored) {
        if (state.draftRestored) {
            kotlinx.coroutines.delay(3000L)
            billingViewModel.clearDraftRestored()
        }
    }

    LaunchedEffect(state.savedBillId) {
        state.savedBillId?.let { billId ->
            billingViewModel.consumeSaved()
            navController.navigate(Routes.billCompleted(billId)) {
                popUpTo(Routes.BILLING) { inclusive = true }
            }
        }
    }

    // Move focus to the price field (when a price is needed) or the quantity
    // field when a product is selected, and back to search when the selection
    // is cleared. Done in a LaunchedEffect so the focusRequester is always
    // attached when requested.
    LaunchedEffect(state.selectedProduct, state.showRateEditor, state.showPricePicker, state.scanNonce) {
        if (state.showPricePicker) return@LaunchedEffect
        val product = state.selectedProduct
        if (product != null) {
            if (state.showRateEditor) rateFocus.requestFocus()
            else qtyFocus.requestFocus()
        } else {
            searchFocus.requestFocus()
        }
    }

    BackHandler {
        if (state.items.isNotEmpty()) showDiscardDialog = true
        else navController.popBackStack()
    }

    ScreenScaffold(
        title = if (state.waitingMode) "Add Waiting Bill" else "Billing",
        onBack = {
            if (state.items.isNotEmpty()) showDiscardDialog = true
            else navController.popBackStack()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .imePadding()
        ) {
            Text(
                "Bill No: ${state.billNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (state.draftRestored) {
                Text(
                    "Bill draft restored",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            if (state.waitingMode) {
                Text(
                    "Waiting customer bill",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Spacer(Modifier.height(6.dp))

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = billingViewModel::onSearchQueryChange,
                placeholder = { Text("Search product or scan barcode") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = null)
                },
                trailingIcon = {
                    Row {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = billingViewModel::clearSearch) {
                                Icon(Icons.Filled.Close, contentDescription = "Clear search")
                            }
                        }
                        IconButton(onClick = {
                            scanLauncher.launch(
                                ScanOptions().apply {
                                    setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES)
                                    setPrompt("Scan product barcode")
                                    setBeepEnabled(true)
                                    setOrientationLocked(true)
                                }
                            )
                        }) {
                            Icon(Icons.Filled.PhotoCamera, contentDescription = "Scan barcode")
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchSubmit(state, billingViewModel)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(searchFocus)
            )

            Spacer(Modifier.height(8.dp))

            when {
                state.selectedProduct != null -> SelectedProductCard(
                    state = state,
                    onQtyChange = billingViewModel::onQuantityChange,
                    onRateChange = billingViewModel::onRateChange,
                    onAdjustQty = billingViewModel::adjustPickerQuantity,
                    onToggleRateEditor = billingViewModel::toggleRateEditor,
                    onAdd = {
                        val added = billingViewModel.addSelectedToBill()
                        if (added) keyboard?.hide()
                    },
                    onQtySubmit = {
                        val added = billingViewModel.addSelectedToBill()
                        if (added) keyboard?.hide()
                    },
                    onRateDone = { billingViewModel.closeRateEditor() },
                    qtyFocus = qtyFocus,
                    rateFocus = rateFocus
                )

                state.searchQuery.isNotBlank() -> SearchResultsList(
                    state = state,
                    onSelect = billingViewModel::selectProduct
                )

                else -> RecentProductsSection(
                    recent = state.recentProducts,
                    onSelect = billingViewModel::selectProduct
                )
            }

            Spacer(Modifier.height(8.dp))

            if (state.items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Bill is empty. Search and add products above.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.items, key = { it.key }) { item ->
                        BillItemRow(
                            item = item,
                            allowPriceOverride = state.allowPriceOverride,
                            onAdjust = { delta -> billingViewModel.adjustQuantity(item.key, delta) },
                            onQuantityChange = { qty -> billingViewModel.setItemQuantity(item.key, qty) },
                            onRateChange = { rate -> billingViewModel.setItemRate(item.key, rate) },
                            onDelete = { removingItem = item }
                        )
                        HorizontalDivider()
                    }
                }
            }

            if (!pickerActive) {
                TotalsAndDiscountCard(state = state, onDiscountChange = billingViewModel::setDiscountText)
            }

            state.saveError?.let { ErrorText(it) }
            state.error?.let { ErrorText(it) }

            if (!pickerActive) {
                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { showHoldDialog = true },
                        enabled = state.items.isNotEmpty(),
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (state.waitingMode) "Hold for Waiting Customer" else "Hold Bill")
                    }
                    if (!state.waitingMode) {
                        OutlinedButton(
                            onClick = {
                                if (state.items.isNotEmpty()) showNewBillDialog = true
                                else billingViewModel.startNewBill()
                            },
                            modifier = Modifier.weight(1f).height(52.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("New Bill")
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                if (!state.waitingMode) {
                    Button(
                        onClick = {
                            if (state.items.any { it.ratePaise <= 0L }) {
                                navController.navigate(Routes.pricing("review"))
                            } else {
                                navController.navigate(Routes.BILL_REVIEW)
                            }
                        },
                        enabled = state.items.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Payment")
                    }
                }
            }
        }
    }

    if (showHoldDialog) {
        HoldBillDialog(
            onHold = { reference ->
                billingViewModel.holdBill(reference)
                showHoldDialog = false
            },
            onDismiss = { showHoldDialog = false }
        )
    }

    if (state.showPricePicker) {
        state.selectedProduct?.let { product ->
            AlertDialog(
                onDismissRequest = billingViewModel::cancelPricePick,
                title = { Text(product.name) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Choose a price", style = MaterialTheme.typography.bodyMedium)
                        state.priceOptions.forEach { option ->
                            OutlinedButton(
                                onClick = { billingViewModel.onPriceOptionSelected(option) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    buildString {
                                        append(Money.paiseToDisplay(option.pricePaise))
                                        if (option.unit.isNotBlank()) append(" / ${option.unit}")
                                    }
                                )
                            }
                        }
                        if (state.allowPriceOverride) {
                            HorizontalDivider()
                            OutlinedButton(
                                onClick = billingViewModel::showCustomPriceEditor,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Enter Custom Price", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = billingViewModel::cancelPricePick) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

    if (showNewBillDialog) {
        AlertDialog(
            onDismissRequest = { showNewBillDialog = false },
            title = { Text("Current bill has items") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "What do you want to do?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextButton(
                        onClick = {
                            showNewBillDialog = false
                            showHoldDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Hold Bill", style = MaterialTheme.typography.titleMedium)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showNewBillDialog = false
                    billingViewModel.startNewBill()
                }) {
                    Text("Start New Bill")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewBillDialog = false }) {
                    Text("Continue Current Bill")
                }
            }
        )
    }

    removingItem?.let { item ->
        ConfirmDialog(
            title = "Remove Item",
            message = "Remove ${item.productName} from this bill?",
            confirmText = "Remove",
            dismissText = "Cancel",
            onConfirm = {
                billingViewModel.removeItem(item.key)
                removingItem = null
            },
            onDismiss = { removingItem = null }
        )
    }

    if (showDiscardDialog) {
        ConfirmDialog(
            title = "Discard current bill?",
            message = "This bill has ${state.items.size} item(s). Discard it?",
            confirmText = "Discard Bill",
            dismissText = "Continue Billing",
            onConfirm = {
                billingViewModel.discardBill()
                showDiscardDialog = false
                navController.popBackStack()
            },
            onDismiss = { showDiscardDialog = false }
        )
    }
}

private fun onSearchSubmit(
    state: BillingUiState,
    billingViewModel: BillingViewModel
) {
    if (state.searchQuery.isNotBlank()) {
        val first = state.searchResults.firstOrNull()
        if (first != null) {
            billingViewModel.selectProduct(first)
            return
        }
    }
    val recent = state.recentProducts.firstOrNull()
    if (recent != null) {
        billingViewModel.selectProduct(recent)
    }
}

@Composable
private fun SelectedProductCard(
    state: BillingUiState,
    onQtyChange: (String) -> Unit,
    onRateChange: (String) -> Unit,
    onAdjustQty: (Long) -> Unit,
    onToggleRateEditor: () -> Unit,
    onAdd: () -> Unit,
    onQtySubmit: () -> Unit,
    onRateDone: () -> Unit,
    qtyFocus: FocusRequester,
    rateFocus: FocusRequester
) {
    val product = state.selectedProduct ?: return
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(product.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        buildString {
                            val shownPaise = Money.parseRupeesToPaise(state.rateText)
                                ?: product.sellingPricePaise
                            append(Money.paiseToDisplay(shownPaise))
                            val unit = state.selectedUnit.ifEmpty { product.unit }
                            if (unit.isNotBlank()) append(" / $unit")
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (state.allowPriceOverride) {
                    IconButton(onClick = onToggleRateEditor) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit price",
                            tint = if (state.showRateEditor) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (state.showRateEditor) {
                Spacer(Modifier.height(8.dp))
                NumberField(
                    value = state.rateText,
                    onValueChange = onRateChange,
                    label = "Price ₹",
                    modifier = Modifier.fillMaxWidth().focusRequester(rateFocus),
                    onDone = onRateDone
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                StepperButton(
                    icon = Icons.Filled.Remove,
                    contentDescription = "Decrease quantity",
                    onClick = { onAdjustQty(-1) }
                )
                OutlinedTextField(
                    value = state.quantityText,
                    onValueChange = onQtyChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .focusRequester(qtyFocus),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onQtySubmit() }),
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        textAlign = androidx.compose.ui.text.style.TextAlign.Start
                    ),
                    placeholder = { Text("1") }
                )
                StepperButton(
                    icon = Icons.Filled.Add,
                    contentDescription = "Increase quantity",
                    onClick = { onAdjustQty(1) }
                )
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Add Item")
            }

            if (state.quantityText.isEmpty() || state.quantityText.toBigDecimalOrNull() == null) {
                Spacer(Modifier.height(4.dp))
                ErrorText("Please enter a valid quantity.")
            }
        }
    }
}

@Composable
private fun StepperButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(40.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = contentDescription)
        }
    }
}

@Composable
private fun SearchResultsList(
    state: BillingUiState,
    onSelect: (Product) -> Unit
) {
    if (state.searching && state.searchResults.isEmpty()) {
        Text(
            "Searching…",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }
    if (state.searchResults.isEmpty()) {
        Text(
            "No products found.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 240.dp)
    ) {
        items(state.searchResults, key = { it.id }) { product ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(product) }
                    .padding(horizontal = 4.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(product.name, style = MaterialTheme.typography.titleSmall)
                    if (product.unit.isNotBlank()) {
                        Text(
                            product.unit,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    Money.paiseToDisplay(product.sellingPricePaise),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun RecentProductsSection(
    recent: List<Product>,
    onSelect: (Product) -> Unit
) {
    if (recent.isEmpty()) {
        Text(
            "No products yet. Add them from the Products screen, then come back.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }
    Text(
        "Recently sold",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(6.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(recent, key = { it.id }) { product ->
            Surface(
                onClick = { onSelect(product) },
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(product.name, style = MaterialTheme.typography.titleSmall)
                    Text(
                        Money.paiseToDisplay(product.sellingPricePaise),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun BillItemRow(
    item: DraftItem,
    allowPriceOverride: Boolean,
    onAdjust: (Long) -> Unit,
    onQuantityChange: (String) -> Unit,
    onRateChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    var editingQty by remember { mutableStateOf(false) }
    var editingRate by remember { mutableStateOf(false) }
    var qtyField by remember(item.key) { mutableStateOf(item.quantity) }
    var rateField by remember(item.key) { mutableStateOf(Money.paiseToNumber(item.ratePaise)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(item.productName, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Remove item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (editingQty) {
                OutlinedTextField(
                    value = qtyField,
                    onValueChange = { qtyField = it.filter { c -> c.isDigit() || c == '.' } },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        onQuantityChange(qtyField)
                        editingQty = false
                    }),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = { Text("Qty") }
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    StepperButton(
                        icon = Icons.Filled.Remove,
                        contentDescription = "Decrease quantity",
                        onClick = { onAdjust(-1) }
                    )
                    Text(
                        item.quantity,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clickable {
                                qtyField = item.quantity
                                editingQty = true
                            }
                            .padding(horizontal = 4.dp)
                    )
                    StepperButton(
                        icon = Icons.Filled.Add,
                        contentDescription = "Increase quantity",
                        onClick = { onAdjust(1) }
                    )
                }
            }

            Text(" × ", style = MaterialTheme.typography.bodyMedium)

            if (editingRate) {
                OutlinedTextField(
                    value = rateField,
                    onValueChange = { rateField = it.filter { c -> c.isDigit() || c == '.' } },
                    modifier = Modifier.width(100.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        onRateChange(rateField)
                        editingRate = false
                    }),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = { Text("₹") }
                )
            } else {
                Text(
                    Money.paiseToDisplay(item.ratePaise),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .clickable(enabled = allowPriceOverride) {
                            rateField = Money.paiseToNumber(item.ratePaise)
                            editingRate = true
                        }
                        .padding(horizontal = 4.dp)
                )
            }

            Text(
                " = ${Money.paiseToDisplay(item.amountPaise)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TotalsAndDiscountCard(
    state: BillingUiState,
    onDiscountChange: (String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Subtotal", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                Text(
                    Money.paiseToDisplay(state.subtotalPaise),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Discount", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                OutlinedTextField(
                    value = state.discountText,
                    onValueChange = onDiscountChange,
                    modifier = Modifier.width(140.dp).height(40.dp),
                    singleLine = true,
                    placeholder = { Text("0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Total", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Text(
                    Money.paiseToDisplay(state.totalPaise),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun HoldBillDialog(
    onHold: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var reference by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hold Bill") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Park this bill and continue with another customer.",
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = reference,
                    onValueChange = { reference = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Customer name / token (optional)") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onHold(reference.trim()) }) {
                Text("Hold Bill")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
