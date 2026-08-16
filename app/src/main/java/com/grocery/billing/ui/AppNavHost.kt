package com.grocery.billing.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grocery.billing.ui.billing.BillCompletedScreen
import com.grocery.billing.ui.billing.BillReviewScreen
import com.grocery.billing.ui.billing.BillingScreen
import com.grocery.billing.ui.billing.BillingViewModel
import com.grocery.billing.ui.billing.PricingScreen
import com.grocery.billing.ui.history.BillDetailScreen
import com.grocery.billing.ui.history.BillHistoryScreen
import com.grocery.billing.ui.home.HomeScreen
import com.grocery.billing.ui.products.CsvImportScreen
import com.grocery.billing.ui.products.ProductEditScreen
import com.grocery.billing.ui.products.ProductListScreen
import com.grocery.billing.ui.settings.SettingsScreen
import com.grocery.billing.ui.waiting.WaitingCustomersScreen

@Composable
fun AppNavHost(
    factory: ViewModelFactory,
    billingViewModel: BillingViewModel
) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(navController, factory)
        }
        composable(Routes.PRODUCTS) {
            ProductListScreen(navController, factory)
        }
        composable(
            route = Routes.PRODUCT_EDIT,
            arguments = listOf(
                navArgument(Routes.PRODUCT_EDIT_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            ProductEditScreen(navController, factory)
        }
        composable(Routes.CSV_IMPORT) {
            CsvImportScreen(navController, factory)
        }
        composable(Routes.BILLING) {
            BillingScreen(navController, factory, billingViewModel)
        }
        composable(Routes.BILL_REVIEW) {
            BillReviewScreen(navController, billingViewModel)
        }
        composable(
            route = Routes.BILL_COMPLETED,
            arguments = listOf(
                navArgument(Routes.BILL_COMPLETED_ARG) { type = NavType.LongType }
            )
        ) { entry ->
            BillCompletedScreen(
                navController = navController,
                billId = entry.arguments?.getLong(Routes.BILL_COMPLETED_ARG)
            )
        }
        composable(Routes.HISTORY) {
            BillHistoryScreen(navController, factory)
        }
        composable(
            route = Routes.BILL_DETAIL,
            arguments = listOf(
                navArgument(Routes.BILL_DETAIL_ARG) { type = NavType.LongType }
            )
        ) { entry ->
            BillDetailScreen(
                navController = navController,
                factory = factory,
                billId = entry.arguments?.getLong(Routes.BILL_DETAIL_ARG)
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(navController, factory)
        }
        composable(Routes.WAITING_CUSTOMERS) {
            WaitingCustomersScreen(navController, factory, billingViewModel)
        }
        composable(
            route = Routes.PRICING,
            arguments = listOf(
                navArgument(Routes.PRICING_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "billing"
                }
            )
        ) { entry ->
            PricingScreen(
                navController = navController,
                billingViewModel = billingViewModel,
                next = entry.arguments?.getString(Routes.PRICING_ARG) ?: "billing"
            )
        }
    }
}
