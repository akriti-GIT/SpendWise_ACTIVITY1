package com.example.spendwise.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val month: Int,                  // 1 - 12
    val year: Int,
    val amount: Double,              // Total monthly budget limit
    val categoryBudgetsJson: String = "" // Optional category limits JSON: {"Food":3000.0,"Transport":2000.0}
)
