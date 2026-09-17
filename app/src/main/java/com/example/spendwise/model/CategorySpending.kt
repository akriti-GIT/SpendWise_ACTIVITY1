package com.example.spendwise.model

data class CategorySpending(
    val categoryName: String,
    val totalAmount: Double,
    val percentage: Float,
    val colorHex: String,
    val count: Int
)
