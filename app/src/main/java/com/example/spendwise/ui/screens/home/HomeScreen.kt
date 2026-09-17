package com.example.spendwise.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.ui.components.*
import com.example.spendwise.ui.theme.*
import com.example.spendwise.util.CurrencyUtils
import com.example.spendwise.viewmodel.SpendWiseViewModel

@Composable
fun HomeScreen(
    viewModel: SpendWiseViewModel,
    onSeeAllClicked: () -> Unit,
    onTransactionClicked: (TransactionEntity) -> Unit,
    onBudgetCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val balance by viewModel.totalBalance.collectAsState()
    val income by viewModel.totalIncome.collectAsState()
    val expenses by viewModel.totalExpenses.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val monthlyBudget by viewModel.monthlyBudget.collectAsState()
    val selectedRange by viewModel.selectedTimeRange.collectAsState()

    val budgetAmount = monthlyBudget?.amount ?: 10000.0
    val budgetRemaining = (budgetAmount - expenses).coerceAtLeast(0.0)
    val budgetRatio = if (budgetAmount > 0) (expenses / budgetAmount).toFloat().coerceIn(0f, 1f) else 0f
    val budgetPercentage = (budgetRatio * 100).toInt()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Bar Greeting
        item {
            SpendWiseTopBar(
                studentName = userSettings?.studentName ?: "Maya Chen"
            )
        }

        // Hero Balance Card
        item {
            BalanceCard(
                balance = balance,
                selectedRange = selectedRange,
                onRangeSelected = { viewModel.setTimeRange(it) }
            )
        }

        // Summary Cards: Income & Spent
        item {
            SummaryCardsRow(
                income = income,
                expenses = expenses,
                incomeSubtitle = "Job + allowance",
                expensesCount = recentTransactions.size
            )
        }

        // Monthly Budget Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                    .clickable { onBudgetCardClicked() }
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "SEPTEMBER BUDGET",
                            fontFamily = SansFamily,
                            fontSize = 9.sp,
                            letterSpacing = 1.8.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${CurrencyUtils.formatAmount(budgetRemaining)} left",
                            fontFamily = SansFamily,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Budget Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(99.dp))
                            .background(TrackBg)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(budgetRatio)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(99.dp))
                                .background(if (budgetPercentage > 85) WarmBrown else DarkPine)
                        )
                    }

                    Spacer(modifier = Modifier.height(7.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${CurrencyUtils.formatAmount(expenses)} of ${CurrencyUtils.formatAmount(budgetAmount)}",
                            fontSize = 10.5.sp,
                            fontFamily = SansFamily,
                            color = TextSecondary
                        )
                        Text(
                            text = "$budgetPercentage% used",
                            fontSize = 10.5.sp,
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Medium,
                            color = if (budgetPercentage > 85) WarmBrown else Terracotta
                        )
                    }
                }
            }
        }

        // Recent Transactions Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT",
                    fontFamily = SansFamily,
                    fontSize = 9.5.sp,
                    letterSpacing = 2.0.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "See all",
                    fontFamily = SansFamily,
                    fontSize = 11.5.sp,
                    color = Terracotta,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onSeeAllClicked() }
                )
            }
        }

        // Recent Transactions List
        if (recentTransactions.isEmpty()) {
            item {
                EmptyState(
                    title = "No Recent Transactions",
                    subtitle = "Add your first expense or income to see it here."
                )
            }
        } else {
            items(recentTransactions) { tx ->
                TransactionItem(
                    transaction = tx,
                    onClick = { onTransactionClicked(tx) }
                )
            }
        }
    }
}
