package com.grocery.billing.ui.billing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.grocery.billing.money.Money
import com.grocery.billing.print.BillLine
import com.grocery.billing.print.BillPrinter
import com.grocery.billing.share.BillTextBuilder
import com.grocery.billing.ui.Routes
import com.grocery.billing.ui.components.ErrorText
import com.grocery.billing.ui.components.LargeButton
import com.grocery.billing.ui.components.LargeOutlinedButton
import com.grocery.billing.ui.settings.rememberShopSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillReviewScreen(
    navController: NavHostController,
    billingViewModel: BillingViewModel
) {
    val state by billingViewModel.state.collectAsState()
    val shop = rememberShopSettings()
    val context = LocalContext.current
    var showShare by remember { mutableStateOf(false) }

    LaunchedEffect(state.savedBillId) {
        state.savedBillId?.let { billId ->
            billingViewModel.consumeSaved()
            navController.navigate(Routes.billCompleted(billId)) {
                popUpTo(Routes.BILLING) { inclusive = true }
            }
        }
    }

    val itemsForBill = state.items.map {
        BillLine(it.productName, it.quantity, it.ratePaise, it.amountPaise)
    }
    val textLines = state.items.map {
        BillTextBuilder.Line(it.productName, it.quantity, it.ratePaise, it.amountPaise)
    }

    val billText = remember(state.items, state.subtotalPaise, state.discountPaise, state.totalPaise, shop) {
        BillTextBuilder.build(
            shopName = shop?.shopName ?: "My Shop",
            address = shop?.address.orEmpty(),
            phone = shop?.phone.orEmpty(),
            showAddress = shop?.showShopAddress ?: true,
            billNumber = state.billNumber,
            date = state.billDate,
            time = state.billTime,
            items = textLines,
            subtotalPaise = state.subtotalPaise,
            discountPaise = state.discountPaise,
            totalPaise = state.totalPaise,
            thankYou = shop?.customerThankYou ?: "Thank you for shopping with us!"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bill Review", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                shop?.shopName ?: "My Shop",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            if (shop?.showShopAddress == true && !shop.address.isNullOrBlank()) {
                Text(
                    shop.address,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            if (!shop?.phone.isNullOrBlank()) {
                Text(
                    "Phone: ${shop?.phone}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            Text("Bill No: ${state.billNumber}", style = MaterialTheme.typography.titleMedium)
            Text("Date: ${state.billDate}", style = MaterialTheme.typography.bodyLarge)
            Text("Time: ${state.billTime}", style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            if (state.items.isEmpty()) {
                Text("No items in this bill.", style = MaterialTheme.typography.bodyLarge)
            } else {
                state.items.forEach { item ->
                    ReviewItemLine(item)
                }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            TotalRow("Subtotal", Money.paiseToDisplay(state.subtotalPaise))
            if (state.discountPaise > 0) {
                TotalRow("Discount", "-${Money.paiseToDisplay(state.discountPaise)}")
            }
            Spacer(Modifier.height(4.dp))
            TotalRow("TOTAL", Money.paiseToDisplay(state.totalPaise), bold = true)

            Spacer(Modifier.height(12.dp))

            Text(
                shop?.thankYou ?: "Thank you!",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LargeButton(
                    text = "PRINT",
                    onClick = {
                        BillPrinter.print(
                            context = context,
                            shopName = shop?.shopName ?: "My Shop",
                            address = shop?.address.orEmpty(),
                            phone = shop?.phone.orEmpty(),
                            showAddress = shop?.showShopAddress ?: true,
                            billNumber = state.billNumber,
                            date = state.billDate,
                            time = state.billTime,
                            items = itemsForBill,
                            subtotalPaise = state.subtotalPaise,
                            discountPaise = state.discountPaise,
                            totalPaise = state.totalPaise,
                            thankYou = shop?.thankYou ?: "Thank you!"
                        )
                    },
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = androidx.compose.ui.graphics.Color.White,
                    height = 60.dp
                )
                LargeButton(
                    text = "SHARE",
                    onClick = { showShare = true },
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = androidx.compose.ui.graphics.Color.White,
                    height = 60.dp
                )
            }

            Spacer(Modifier.height(8.dp))
            LargeOutlinedButton(text = "EDIT", onClick = { navController.popBackStack() })
            Spacer(Modifier.height(8.dp))
            LargeButton(
                text = "FINISH BILL",
                onClick = { billingViewModel.saveBill() },
                enabled = state.items.isNotEmpty() && !state.saving,
                height = 64.dp
            )
            state.saveError?.let { ErrorText(it) }
        }
    }

    if (showShare) {
        ShareBillDialog(text = billText, onDismiss = { showShare = false })
    }
}

@Composable
private fun ReviewItemLine(item: DraftItem) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(item.productName, style = MaterialTheme.typography.titleMedium)
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "${item.quantity} × ${Money.paiseToDisplay(item.ratePaise)}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                "= ${Money.paiseToDisplay(item.amountPaise)}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun TotalRow(label: String, value: String, bold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(
            label,
            style = if (bold) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            value,
            style = if (bold) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
            color = if (bold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
