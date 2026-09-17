package com.example.spendwise.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Transactions : Screen("transactions")
    object AddTransaction : Screen("add_transaction")
    object Analytics : Screen("analytics")
    object Budget : Screen("budget")
    object Settings : Screen("settings")
}
