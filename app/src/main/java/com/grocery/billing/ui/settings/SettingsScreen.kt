package com.grocery.billing.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grocery.billing.GroceryApp
import com.grocery.billing.data.backup.BackupManager
import com.grocery.billing.ui.ViewModelFactory
import com.grocery.billing.ui.components.ConfirmDialog
import com.grocery.billing.ui.components.LargeButton
import com.grocery.billing.ui.components.LargeOutlinedButton
import com.grocery.billing.ui.components.ScreenScaffold
import com.grocery.billing.ui.components.SectionHeader
import kotlinx.coroutines.launch
import java.io.StringReader

@Composable
fun SettingsScreen(navController: NavHostController, factory: ViewModelFactory) {
    val viewModel: SettingsViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val backupManager = remember {
        BackupManager(context.applicationContext)
    }

    var pendingRestoreJson by remember { mutableStateOf<String?>(null) }
    var restoreMessage by remember { mutableStateOf<String?>(null) }

    val createFile = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val json = backupManager.buildBackupJson()
                try {
                    context.contentResolver.openOutputStream(uri)?.use { out ->
                        out.write(json.toByteArray(Charsets.UTF_8))
                    }
                    restoreMessage = "Backup exported."
                } catch (e: Exception) {
                    restoreMessage = "Export failed."
                }
            }
        }
    }

    val openFile = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val text = try {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes().toString(Charsets.UTF_8) } ?: ""
            } catch (e: Exception) {
                ""
            }
            if (text.isNotBlank()) pendingRestoreJson = text
        }
    }

    ScreenScaffold(title = "Settings", onBack = { navController.popBackStack() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SectionHeader("Shop Details")
            OutlinedTextField(
                value = state.shopName,
                onValueChange = viewModel::onShopNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Shop Name") }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.address,
                onValueChange = viewModel::onAddressChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Address") }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.phone,
                onValueChange = viewModel::onPhoneChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone Number") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
                )
            )

            Spacer(Modifier.height(16.dp))
            SectionHeader("Currency")
            OutlinedTextField(
                value = state.currencySymbol,
                onValueChange = viewModel::onCurrencyChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Currency Symbol") }
            )

            Spacer(Modifier.height(16.dp))
            SectionHeader("Receipt")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Show shop address on bill", style = MaterialTheme.typography.bodyLarge)
                }
                Switch(
                    checked = state.showShopAddress,
                    onCheckedChange = viewModel::onShowAddressChange
                )
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.thankYou,
                onValueChange = viewModel::onThankYouChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Thank-you Message") }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.customerThankYou,
                onValueChange = viewModel::onCustomerThankYouChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Customer Thank-you Message (for WhatsApp/SMS)") }
            )

            Spacer(Modifier.height(16.dp))
            SectionHeader("Billing")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Allow cashier to change item price", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "When off, the selling price is fixed to the product price.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = state.allowPriceOverride,
                    onCheckedChange = viewModel::onAllowPriceOverrideChange
                )
            }

            Spacer(Modifier.height(16.dp))
            LargeButton(text = "Save Settings", onClick = viewModel::save)
            if (state.saved) {
                Text(
                    "Settings saved.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(24.dp))
            SectionHeader("Data")
            LargeButton(
                text = "Export Backup",
                onClick = {
                    createFile.launch("myshop_backup_${System.currentTimeMillis()}.json")
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = androidx.compose.ui.graphics.Color.White
            )
            Spacer(Modifier.height(8.dp))
            LargeOutlinedButton(
                text = "Import Backup",
                onClick = {
                    openFile.launch(arrayOf("application/json", "text/*"))
                }
            )

            restoreMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    pendingRestoreJson?.let { json ->
        ConfirmDialog(
            title = "Restore Backup",
            message = "This will replace ALL current products, bills and settings with the backup data. Continue?",
            confirmText = "Restore",
            dismissText = "Cancel",
            onConfirm = {
                scope.launch {
                    val result = backupManager.restoreFrom(StringReader(json))
                    restoreMessage = result.error ?: "Backup restored successfully."
                    pendingRestoreJson = null
                }
            },
            onDismiss = { pendingRestoreJson = null }
        )
    }
}
