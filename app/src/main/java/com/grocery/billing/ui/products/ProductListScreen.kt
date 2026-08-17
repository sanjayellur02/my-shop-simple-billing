package com.grocery.billing.ui.products

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grocery.billing.data.entity.Product
import com.grocery.billing.ui.ViewModelFactory
import com.grocery.billing.ui.Routes
import com.grocery.billing.ui.components.ConfirmDialog
import com.grocery.billing.ui.components.EmptyState
import com.grocery.billing.ui.components.LargeButton
import com.grocery.billing.ui.components.ScreenScaffold

@Composable
fun ProductListScreen(navController: NavHostController, factory: ViewModelFactory) {
    val viewModel: ProductListViewModel = viewModel(factory = factory)
    val products by viewModel.products.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectionMode by viewModel.selectionMode.collectAsState()
    val selected by viewModel.selected.collectAsState()
    var pendingDelete by remember { mutableStateOf<Product?>(null) }
    var pendingBulkDelete by remember { mutableStateOf(false) }

    ScreenScaffold(title = "Products", onBack = { navController.popBackStack() }) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            if (selectionMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = viewModel::exitSelectionMode) {
                        Text("Cancel")
                    }
                    Text(
                        text = if (selected.size == products.size) "All ${selected.size} selected"
                        else "${selected.size} selected",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    TextButton(
                        onClick = {
                            if (selected.size == products.size) viewModel.clearSelection()
                            else viewModel.selectAll()
                        }
                    ) {
                        Text(if (selected.size == products.size) "Clear" else "Select All")
                    }
                    IconButton(
                        onClick = { pendingBulkDelete = true },
                        enabled = selected.isNotEmpty()
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete selected",
                            tint = if (selected.isNotEmpty()) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            } else {
                LargeButton(
                    text = "Add Product",
                    onClick = { navController.navigate(Routes.productEdit(null)) }
                )
                Spacer(Modifier.height(12.dp))
                LargeOutlinedSmallButton(
                    text = "Import from CSV",
                    onClick = { navController.navigate(Routes.CSV_IMPORT) }
                )
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = viewModel::enterSelectionMode,
                    enabled = products.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text("Bulk Delete")
                }
                Spacer(Modifier.height(12.dp))
            }

            if (products.isEmpty()) {
                EmptyState("No products yet.\nAdd products or import from CSV.")
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(products, key = { it.id }) { product ->
                        if (selectionMode) {
                            val isSelected = product.id in selected
                            ProductSelectRow(
                                product = product,
                                selected = isSelected,
                                onToggle = { viewModel.toggleSelected(product.id) }
                            )
                        } else {
                            ProductRow(
                                product = product,
                                onEdit = { navController.navigate(Routes.productEdit(product.id)) },
                                onDelete = { pendingDelete = product }
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }
            }

            if (error != null) {
                Text(error.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    pendingDelete?.let { product ->
        ConfirmDialog(
            title = "Delete Product",
            message = "Are you sure you want to delete this product?",
            confirmText = "Delete",
            dismissText = "Cancel",
            onConfirm = {
                viewModel.delete(product)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null }
        )
    }

    if (pendingBulkDelete) {
        ConfirmDialog(
            title = "Delete ${selected.size} Product(s)?",
            message = "Are you sure you want to delete the selected products? Old bills keep their saved product names.",
            confirmText = "Delete",
            dismissText = "Cancel",
            onConfirm = {
                viewModel.deleteSelected()
                pendingBulkDelete = false
            },
            onDismiss = { pendingBulkDelete = false }
        )
    }
}

@Composable
private fun ProductSelectRow(
    product: Product,
    selected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = { onToggle() }
        )
        Text(
            text = product.id,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 12.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = productPriceLabel(product),
                style = MaterialTheme.typography.bodySmall,
                color = if (product.sellingPricePaise > 0L) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }
    }
}

@Composable
private fun ProductRow(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = product.id,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 12.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = productPriceLabel(product),
                style = MaterialTheme.typography.bodySmall,
                color = if (product.sellingPricePaise > 0L) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }
        IconButton(onClick = onEdit) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
        }
    }
}

private fun productPriceLabel(product: Product): String {
    if (product.sellingPricePaise <= 0L) return "No price set"
    val price = com.grocery.billing.money.Money.paiseToDisplay(product.sellingPricePaise)
    return if (product.unit.isNotBlank()) "$price / ${product.unit}" else price
}

@Composable
private fun LargeOutlinedSmallButton(text: String, onClick: () -> Unit) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
        Text(text)
    }
}
