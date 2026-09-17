package com.example.spendwise.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val currency: String = "₹",
    val darkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val studentName: String = "Maya Chen",
    val collegeId: String = "MCA-2026-087",
    val monthlyBudget: Double = 10000.0
)
