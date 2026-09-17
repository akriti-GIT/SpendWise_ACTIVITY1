package com.example.spendwise.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.ui.components.SpendWiseBottomBar
import com.example.spendwise.ui.screens.addtransaction.AddTransactionScreen
import com.example.spendwise.ui.screens.analytics.AnalyticsScreen
import com.example.spendwise.ui.screens.budget.BudgetScreen
import com.example.spendwise.ui.screens.home.HomeScreen
import com.example.spendwise.ui.screens.settings.SettingsScreen
import com.example.spendwise.ui.screens.transactions.TransactionsScreen
import com.example.spendwise.viewmodel.SpendWiseViewModel

@Composable
fun SpendWiseNavGraph(
    viewModel: SpendWiseViewModel,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var editingTransaction by remember { mutableStateOf<TransactionEntity?>(null) }

    Scaffold(
        bottomBar = {
            // Hide bottom bar on Add Transaction screen as in reference UI
            if (currentRoute != Screen.AddTransaction.route) {
                SpendWiseBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { targetRoute ->
                        if (targetRoute == Screen.AddTransaction.route) {
                            editingTransaction = null
                        }
                        navController.navigate(targetRoute) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onSeeAllClicked = { navController.navigate(Screen.Transactions.route) },
                    onTransactionClicked = { tx ->
                        editingTransaction = tx
                        navController.navigate(Screen.AddTransaction.route)
                    },
                    onBudgetCardClicked = { navController.navigate(Screen.Budget.route) }
                )
            }

            composable(Screen.Transactions.route) {
                TransactionsScreen(
                    viewModel = viewModel,
                    onEditTransaction = { tx ->
                        editingTransaction = tx
                        navController.navigate(Screen.AddTransaction.route)
                    }
                )
            }

            composable(Screen.AddTransaction.route) {
                AddTransactionScreen(
                    viewModel = viewModel,
                    editingTransaction = editingTransaction,
                    onNavigateBack = {
                        editingTransaction = null
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Analytics.route) {
                AnalyticsScreen(viewModel = viewModel)
            }

            composable(Screen.Budget.route) {
                BudgetScreen(viewModel = viewModel)
            }

            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
