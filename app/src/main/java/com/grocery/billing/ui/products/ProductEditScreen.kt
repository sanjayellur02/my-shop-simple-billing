package com.grocery.billing.ui.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grocery.billing.ui.ViewModelFactory
import com.grocery.billing.ui.components.ErrorText
import com.grocery.billing.ui.components.LargeButton
import com.grocery.billing.ui.components.LargeOutlinedButton
import com.grocery.billing.ui.components.ScreenScaffold

@Composable
fun ProductEditScreen(navController: NavHostController, factory: ViewModelFactory) {
    val viewModel: ProductEditViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()
    val isEditing = state.isEditing

    LaunchedEffect(state.saved) {
        if (state.saved) navController.popBackStack()
    }

    ScreenScaffold(
        title = if (isEditing) "Edit Product" else "Add Product",
        onBack = { navController.popBackStack() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.id,
                onValueChange = viewModel::onIdChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Product ID") },
                textStyle = MaterialTheme.typography.titleLarge
            )
            if (isEditing) {
                Text(
                    "The ID must stay unique. Old bills keep their saved product names.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Product Name") },
                textStyle = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = state.priceText,
                onValueChange = viewModel::onPriceChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Selling Price ₹") },
                placeholder = { Text("0") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                ),
                textStyle = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = state.unit,
                onValueChange = viewModel::onUnitChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Unit") },
                placeholder = { Text("kg, pcs, ltr, pack (optional)") },
                textStyle = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = state.barcode,
                onValueChange = viewModel::onBarcodeChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Barcode") },
                placeholder = { Text("Scan barcode (optional)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                ),
                textStyle = MaterialTheme.typography.titleLarge
            )

            state.error?.let { ErrorText(it) }

            Spacer(Modifier.height(8.dp))

            LargeButton(
                text = "Save",
                onClick = viewModel::save
            )
            Spacer(Modifier.height(8.dp))
            LargeOutlinedButton(
                text = "Cancel",
                onClick = { navController.popBackStack() }
            )
        }
    }
}
