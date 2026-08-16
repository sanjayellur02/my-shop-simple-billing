package com.grocery.billing.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grocery.billing.data.entity.BillItem
import com.grocery.billing.money.Money
import com.grocery.billing.print.BillLine
import com.grocery.billing.print.BillPrinter
import com.grocery.billing.share.BillTextBuilder
import com.grocery.billing.share.ShareBillData
import com.grocery.billing.ui.ViewModelFactory
import com.grocery.billing.ui.billing.ShareBillDialog
import com.grocery.billing.ui.components.ErrorText
import com.grocery.billing.ui.components.LargeButton
import com.grocery.billing.ui.components.ScreenScaffold
import com.grocery.billing.ui.components.SectionHeader
import com.grocery.billing.ui.settings.rememberShopSettings

@Composable
fun BillDetailScreen(
    navController: NavHostController,
    factory: ViewModelFactory,
    billId: Long?
) {
    val viewModel: BillDetailViewModel = viewModel(factory = factory)
    val data by viewModel.billWithItems.collectAsState()
    val shop = rememberShopSettings()
    val context = LocalContext.current
    var showShare by remember { mutableStateOf(false) }

    val bill = data?.bill
    val items = data?.items ?: emptyList()

    ScreenScaffold(
        title = "Bill Details",
        onBack = { navController.popBackStack() }
    ) {
        if (bill == null) {
            Text("Loading bill...", modifier = Modifier.padding(16.dp))
            return@ScreenScaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
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
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            Text("Bill No: ${bill.billNumber}", style = MaterialTheme.typography.titleMedium)
            Text("Date: ${bill.billDate}", style = MaterialTheme.typography.bodyLarge)
            Text("Time: ${bill.billTime}", style = MaterialTheme.typography.bodyLarge)

            SectionHeader("Items")

            items.forEach { item ->
                DetailItemLine(item)
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            DetailTotalRow("Subtotal", Money.paiseToDisplay(bill.subtotalPaise))
            if (bill.discountPaise > 0) {
                DetailTotalRow("Discount", "-${Money.paiseToDisplay(bill.discountPaise)}")
            }
            Spacer(Modifier.height(4.dp))
            DetailTotalRow("TOTAL", Money.paiseToDisplay(bill.totalPaise), bold = true)

            Spacer(Modifier.height(8.dp))
            Text(
                shop?.thankYou ?: "Thank you!",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            val billLines = items.map {
                BillLine(it.productNameSnapshot, it.quantity, it.ratePaise, it.amountPaise)
            }

            Row {
                LargeButton(
                    text = "PRINT",
                    onClick = {
                        BillPrinter.print(
                            context = context,
                            shopName = shop?.shopName ?: "My Shop",
                            address = shop?.address.orEmpty(),
                            phone = shop?.phone.orEmpty(),
                            showAddress = shop?.showShopAddress ?: true,
                            billNumber = bill.billNumber,
                            date = bill.billDate,
                            time = bill.billTime,
                            items = billLines,
                            subtotalPaise = bill.subtotalPaise,
                            discountPaise = bill.discountPaise,
                            totalPaise = bill.totalPaise,
                            thankYou = shop?.thankYou ?: "Thank you!"
                        )
                    },
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = androidx.compose.ui.graphics.Color.White
                )
                Spacer(Modifier.height(0.dp))
                LargeButton(
                    text = "SHARE",
                    onClick = { showShare = true },
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = androidx.compose.ui.graphics.Color.White
                )
            }
        }
    }

    if (showShare && data != null) {
        val text = BillTextBuilder.buildForBill(
            billWithItems = data!!,
            shopName = shop?.shopName ?: "My Shop",
            address = shop?.address.orEmpty(),
            phone = shop?.phone.orEmpty(),
            showAddress = shop?.showShopAddress ?: true,
            thankYou = shop?.customerThankYou ?: "Thank you for shopping with us!"
        )
        val shareItems = data!!.items.map {
            BillLine(it.productNameSnapshot, it.quantity, it.ratePaise, it.amountPaise)
        }
        ShareBillDialog(
            data = ShareBillData(
                shopName = shop?.shopName ?: "My Shop",
                address = shop?.address.orEmpty(),
                phone = shop?.phone.orEmpty(),
                showAddress = shop?.showShopAddress ?: true,
                billNumber = data!!.bill.billNumber,
                date = data!!.bill.billDate,
                time = data!!.bill.billTime,
                items = shareItems,
                subtotalPaise = data!!.bill.subtotalPaise,
                discountPaise = data!!.bill.discountPaise,
                totalPaise = data!!.bill.totalPaise,
                thankYou = shop?.thankYou ?: "Thank you!",
                text = text
            ),
            onDismiss = { showShare = false }
        )
    }
}

@Composable
private fun DetailItemLine(item: BillItem) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(item.productNameSnapshot, style = MaterialTheme.typography.titleMedium)
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
private fun DetailTotalRow(label: String, value: String, bold: Boolean = false) {
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
