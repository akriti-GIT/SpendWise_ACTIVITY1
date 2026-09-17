package com.example.spendwise.model

data class DailySpending(
    val dayLabel: String,       // e.g. "M", "T", "W"
    val dateEpochDay: Long,
    val amount: Double,
    val isPeak: Boolean = false
)
