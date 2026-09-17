package com.example.spendwise.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spendwise.model.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val type: TransactionType = TransactionType.EXPENSE
)
