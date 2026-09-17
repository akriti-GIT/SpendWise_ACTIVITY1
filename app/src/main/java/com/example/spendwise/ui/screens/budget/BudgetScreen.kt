package com.example.spendwise.ui.screens.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.ui.components.getCategoryColor
import com.example.spendwise.ui.theme.*
import com.example.spendwise.util.CurrencyUtils
import com.example.spendwise.viewmodel.SpendWiseViewModel

@Composable
fun BudgetScreen(
    viewModel: SpendWiseViewModel,
    modifier: Modifier = Modifier
) {
    val monthlyBudget by viewModel.monthlyBudget.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val categoryBreakdown by viewModel.categorySpending.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var editAmountText by remember { mutableStateOf("") }

    val budgetAmount = monthlyBudget?.amount ?: 10000.0
    val remaining = (budgetAmount - totalExpenses).coerceAtLeast(0.0)
    val ratio = if (budgetAmount > 0) (totalExpenses / budgetAmount).toFloat().coerceIn(0f, 1f) else 0f
    val percentage = (ratio * 100).toInt()
    val isNearLimit = percentage >= 80

    // Category budgets mockup / calculated
    val categoryBudgetLimits = mapOf(
        "Food" to 3000.0,
        "Transport" to 2000.0,
        "Education" to 2000.0,
        "Shopping" to 1500.0,
        "Entertainment" to 1000.0,
        "Other" to 500.0
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "SEPTEMBER",
                        fontFamily = SansFamily,
                        fontSize = 9.5.sp,
                        letterSpacing = 2.0.sp,
                        color = Terracotta,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Budgets",
                        fontFamily = SerifFamily,
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = "Edit",
                    fontFamily = SansFamily,
                    fontSize = 12.sp,
                    color = Terracotta,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        editAmountText = String.format("%.0f", budgetAmount)
                        showEditDialog = true
                    }
                )
            }
        }

        // Main Monthly Budget Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(DarkPine)
                    .padding(22.dp)
            ) {
                Column {
                    Text(
                        text = "MONTHLY BUDGET",
                        fontFamily = SansFamily,
                        fontSize = 9.5.sp,
                        letterSpacing = 2.0.sp,
                        color = Color.White.copy(alpha = 0.55f),
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = CurrencyUtils.formatAmount(remaining),
                            fontFamily = SerifFamily,
                            fontSize = 40.sp,
                            color = CardLight,
                            lineHeight = 44.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "left of ${CurrencyUtils.formatAmount(budgetAmount)}",
                            fontFamily = SansFamily,
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(99.dp))
                            .background(Color.White.copy(alpha = 0.16f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(ratio)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(99.dp))
                                .background(if (isNearLimit) WarmBrown else MintGreen)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "18 days elapsed",
                            fontFamily = SansFamily,
                            fontSize = 10.5.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                        Text(
                            text = if (isNearLimit) "⚠️ Warning: $percentage% used" else "✓ On track ($percentage%)",
                            fontFamily = SansFamily,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isNearLimit) Peach else MintGreen
                        )
                    }
                }
            }
        }

        // By Category Header
        item {
            Text(
                text = "BY CATEGORY",
                fontFamily = SansFamily,
                fontSize = 9.5.sp,
                letterSpacing = 2.0.sp,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Category Budgets List
        categoryBudgetLimits.forEach { (catName, limit) ->
            val spent = categoryBreakdown.find { it.categoryName.equals(catName, ignoreCase = true) }?.totalAmount ?: 0.0
            val catRatio = (spent / limit).toFloat().coerceIn(0f, 1f)
            val catRemaining = limit - spent
            val isOver = spent > limit
            val isTight = catRatio >= 0.85f

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .clip(CircleShape)
                                        .background(getCategoryColor(catName))
                                )
                                Spacer(modifier = Modifier.width(9.dp))
                                Text(
                                    text = catName,
                                    fontSize = 14.sp,
                                    fontFamily = SansFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "${CurrencyUtils.formatAmount(spent)} / ${CurrencyUtils.formatAmount(limit)}",
                                fontSize = 12.sp,
                                fontFamily = SansFamily,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(9.dp))

                        // Category Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(99.dp))
                                .background(TrackBg)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(catRatio)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(99.dp))
                                    .background(if (isOver) WarmBrown else getCategoryColor(catName))
                            )
                        }

                        Spacer(modifier = Modifier.height(7.dp))

                        val statusText = when {
                            isOver -> "${CurrencyUtils.formatAmount(spent - limit)} over budget"
                            isTight -> "${CurrencyUtils.formatAmount(catRemaining)} left · tight"
                            else -> "${CurrencyUtils.formatAmount(catRemaining)} left"
                        }

                        Text(
                            text = statusText,
                            fontSize = 10.5.sp,
                            fontFamily = SansFamily,
                            color = if (isOver || isTight) WarmBrown else TextSecondary
                        )
                    }
                }
            }
        }
    }

    // Edit Budget Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            shape = RoundedCornerShape(22.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Set Monthly Budget",
                    fontFamily = SerifFamily,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter your target monthly budget for college expenses:",
                        fontSize = 13.sp,
                        fontFamily = SansFamily,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editAmountText,
                        onValueChange = { editAmountText = it },
                        label = { Text("Monthly Budget (₹)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newAmount = editAmountText.toDoubleOrNull()
                        if (newAmount != null && newAmount > 0) {
                            viewModel.setMonthlyBudget(newAmount)
                            showEditDialog = false
                        }
                    },
                    shape = RoundedCornerShape(99.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkPine)
                ) {
                    Text("Save", fontFamily = SansFamily)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", fontFamily = SansFamily, color = TextSecondary)
                }
            }
        )
    }
}
