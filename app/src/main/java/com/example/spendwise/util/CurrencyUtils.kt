package com.example.spendwise.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val indianFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 0
    }

    fun formatAmount(amount: Double, symbol: String = "₹"): String {
        // Format with commas: ₹12,450
        val isWhole = amount % 1.0 == 0.0
        val pattern = if (isWhole) "%,.0f" else "%,.2f"
        val formattedNumber = String.format(Locale("en", "IN"), pattern, amount)
        return "$symbol$formattedNumber"
    }

    fun formatPlain(amount: Double): String {
        val isWhole = amount % 1.0 == 0.0
        return if (isWhole) String.format(Locale.US, "%.0f", amount) else String.format(Locale.US, "%.2f", amount)
    }
}
