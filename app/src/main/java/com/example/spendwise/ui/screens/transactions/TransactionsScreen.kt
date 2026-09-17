package com.example.spendwise.ui.screens.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.model.SortOrder
import com.example.spendwise.model.TransactionType
import com.example.spendwise.ui.components.EmptyState
import com.example.spendwise.ui.components.TransactionItem
import com.example.spendwise.ui.theme.*
import com.example.spendwise.util.CurrencyUtils
import com.example.spendwise.util.DateUtils
import com.example.spendwise.viewmodel.SpendWiseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    viewModel: SpendWiseViewModel,
    onEditTransaction: (TransactionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.filteredTransactions.collectAsState()
    val income by viewModel.totalIncome.collectAsState()
    val expenses by viewModel.totalExpenses.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedType by viewModel.selectedTypeFilter.collectAsState()
    val selectedCategory by viewModel.selectedCategoryFilter.collectAsState()
    val selectedSort by viewModel.selectedSortOrder.collectAsState()

    var selectedTransactionForDetail by remember { mutableStateOf<TransactionEntity?>(null) }
    var showSortMenu by remember { mutableStateOf(false) }

    // Categories filter options
    val filterCategories = listOf("All", "Food", "Transport", "Education", "Shopping", "Entertainment", "Health", "Bills", "Other")

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
                        text = "HISTORY",
                        fontFamily = SansFamily,
                        fontSize = 9.5.sp,
                        letterSpacing = 2.0.sp,
                        color = Terracotta,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Activity",
                        fontFamily = SerifFamily,
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Month Pill
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, CardBorder, CircleShape)
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "September 2026 ▾",
                        fontFamily = SansFamily,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Out / In summary pills
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Out
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkPine)
                        .padding(horizontal = 15.dp, vertical = 13.dp)
                ) {
                    Column {
                        Text(
                            text = "OUT",
                            fontFamily = SansFamily,
                            fontSize = 9.sp,
                            letterSpacing = 1.8.sp,
                            color = Color.White.copy(alpha = 0.55f),
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = CurrencyUtils.formatAmount(expenses),
                            fontFamily = SerifFamily,
                            fontSize = 24.sp,
                            color = CardLight
                        )
                    }
                }

                // In
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 15.dp, vertical = 13.dp)
                ) {
                    Column {
                        Text(
                            text = "IN",
                            fontFamily = SansFamily,
                            fontSize = 9.sp,
                            letterSpacing = 1.8.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = CurrencyUtils.formatAmount(income),
                            fontFamily = SerifFamily,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search transactions, notes, categories...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search", tint = TextMuted) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = DarkPine,
                    unfocusedBorderColor = CardBorder
                ),
                singleLine = true
            )
        }

        // Filter chips (Type & Category)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Type filter: All / Expense / Income
                listOf(
                    "All" to null,
                    "Expenses" to TransactionType.EXPENSE,
                    "Income" to TransactionType.INCOME
                ).forEach { (label, type) ->
                    val isSelected = selectedType == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setTypeFilter(type) },
                        label = { Text(label, fontSize = 11.5.sp) },
                        shape = CircleShape,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkPine,
                            selectedLabelColor = CardLight,
                            containerColor = Color.Transparent,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = CardBorder,
                            selectedBorderColor = DarkPine
                        )
                    )
                }

                // Sort Dropdown Button
                Box {
                    AssistChip(
                        onClick = { showSortMenu = true },
                        label = { Text("Sort: ${selectedSort.label}", fontSize = 11.5.sp) },
                        shape = CircleShape,
                        border = AssistChipDefaults.assistChipBorder(enabled = true, borderColor = CardBorder)
                    )
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        SortOrder.values().forEach { order ->
                            DropdownMenuItem(
                                text = { Text(order.label) },
                                onClick = {
                                    viewModel.setSortOrder(order)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }

                // Category chips
                filterCategories.forEach { cat ->
                    val isSelected = if (cat == "All") selectedCategory == null else selectedCategory.equals(cat, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            viewModel.setCategoryFilter(if (cat == "All") null else cat)
                        },
                        label = { Text(cat, fontSize = 11.5.sp) },
                        shape = CircleShape,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkPine,
                            selectedLabelColor = CardLight,
                            containerColor = Color.Transparent,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = CardBorder,
                            selectedBorderColor = DarkPine
                        )
                    )
                }
            }
        }

        // Transactions Grouped by Date
        if (transactions.isEmpty()) {
            item {
                EmptyState(
                    title = "No Transactions Found",
                    subtitle = "Try adjusting your search query or filter settings."
                )
            }
        } else {
            val groupedByDate = transactions.groupBy { DateUtils.getRelativeDateHeader(it.date) }
            groupedByDate.forEach { (dateHeader, txsInGroup) ->
                val dayTotal = txsInGroup.sumOf { if (it.type == TransactionType.EXPENSE) -it.amount else it.amount }
                val daySign = if (dayTotal < 0) "−" else "+"

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dateHeader.uppercase(),
                            fontFamily = SansFamily,
                            fontSize = 9.5.sp,
                            letterSpacing = 1.8.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "$daySign${CurrencyUtils.formatAmount(kotlin.math.abs(dayTotal))}",
                            fontFamily = SansFamily,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                items(txsInGroup) { tx ->
                    TransactionItem(
                        transaction = tx,
                        onClick = { selectedTransactionForDetail = tx }
                    )
                }
            }
        }
    }

    // Detail Bottom Sheet
    TransactionDetailSheet(
        transaction = selectedTransactionForDetail,
        onDismiss = { selectedTransactionForDetail = null },
        onEdit = { tx ->
            selectedTransactionForDetail = null
            onEditTransaction(tx)
        },
        onDelete = { tx ->
            viewModel.deleteTransaction(tx)
            selectedTransactionForDetail = null
        }
    )
}
