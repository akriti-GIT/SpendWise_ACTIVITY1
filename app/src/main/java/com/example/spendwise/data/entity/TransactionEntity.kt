package com.example.spendwise.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spendwise.model.PaymentMethod
import com.example.spendwise.model.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    val category: String,
    val description: String,
    val date: Long,                  // Timestamp in milliseconds
    val paymentMethod: PaymentMethod,
    val createdAt: Long = System.currentTimeMillis()
)
