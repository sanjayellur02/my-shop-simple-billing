package com.grocery.billing.ui.products

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grocery.billing.ui.ViewModelFactory
import com.grocery.billing.ui.components.ErrorText
import com.grocery.billing.ui.components.LargeButton
import com.grocery.billing.ui.components.LargeOutlinedButton
import com.grocery.billing.ui.components.ScreenScaffold

@Composable
fun CsvImportScreen(navController: NavHostController, factory: ViewModelFactory) {
    val viewModel: CsvImportViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val fileName = uri.lastPathSegment?.substringAfterLast("/") ?: "products.csv"
            val text = try {
                context.contentResolver.openInputStream(uri)?.use {
                    it.readBytes().toString(Charsets.UTF_8)
                } ?: ""
            } catch (e: Exception) {
                ""
            }
            if (text.length > 5_000_000) {
                viewModel.reset()
                return@rememberLauncherForActivityResult
            }
            if (text.isNotBlank()) viewModel.analyzeText(text, fileName)
        }
    }

    ScreenScaffold(title = "Import Products", onBack = { navController.popBackStack() }) {
        when (val s = state) {
            is CsvImportState.Idle -> IdleContent(
                onPickFile = {
                    filePicker.launch(arrayOf("text/*", "text/comma-separated-values", "application/octet-stream"))
                },
                onCancel = { navController.popBackStack() }
            )

            is CsvImportState.Loaded -> PreviewContent(
                totalRows = s.analysis.totalRows,
                valid = s.analysis.valid.size,
                duplicates = s.analysis.duplicateCount,
                invalid = s.analysis.invalidCount,
                errors = s.analysis.errors,
                headerError = s.analysis.headerError,
                onCancel = { viewModel.reset() },
                onImport = viewModel::import
            )

            CsvImportState.Imported -> ImportedContent(
                onDone = { navController.popBackStack() }
            )

            is CsvImportState.Failed -> {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    ErrorText(s.message)
                    Spacer(Modifier.height(16.dp))
                    LargeButton(text = "Try Again", onClick = viewModel::reset)
                }
            }
        }
    }
}

@Composable
private fun IdleContent(onPickFile: () -> Unit, onCancel: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Choose a CSV file with the columns:",
            style = MaterialTheme.typography.bodyLarge
        )
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("id,product_name,price,unit,barcode", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text("101,Rice,70,kg", style = MaterialTheme.typography.bodyMedium)
                Text("101,Rice,140,2kg", style = MaterialTheme.typography.bodyMedium)
                Text("102,Sugar,45,kg", style = MaterialTheme.typography.bodyMedium)
                Text("103,Sunflower Oil,175,ltr,8901234567890", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(
            "Only id and product_name are required. price, unit and barcode are optional. " +
                "Repeat a product id on more rows to add extra selling prices (the first row is the default price).",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LargeButton(text = "Choose CSV File", onClick = onPickFile)
        LargeOutlinedButton(text = "Cancel", onClick = onCancel)
    }
}

@Composable
private fun PreviewContent(
    totalRows: Int,
    valid: Int,
    duplicates: Int,
    invalid: Int,
    errors: List<String>,
    headerError: String?,
    onCancel: () -> Unit,
    onImport: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Import Preview", style = MaterialTheme.typography.headlineSmall)

        if (headerError != null) {
            ErrorText(headerError)
            Spacer(Modifier.height(8.dp))
            LargeOutlinedButton(text = "Cancel", onClick = onCancel)
            return@Column
        }

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Total rows: $totalRows", style = MaterialTheme.typography.bodyLarge)
                Text("Valid: $valid", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                Text("Duplicate IDs: $duplicates", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                Text("Invalid rows: $invalid", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
            }
        }

        if (errors.isNotEmpty()) {
            Text("Details:", style = MaterialTheme.typography.titleSmall)
            errors.forEach {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }
        }

        Spacer(Modifier.weight(1f))

        if (valid > 0) {
            LargeButton(text = "Import $valid Products", onClick = onImport)
        }
        LargeOutlinedButton(text = "Cancel", onClick = onCancel)
    }
}

@Composable
private fun ImportedContent(onDone: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Products imported successfully.",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        LargeButton(text = "Done", onClick = onDone)
    }
}
