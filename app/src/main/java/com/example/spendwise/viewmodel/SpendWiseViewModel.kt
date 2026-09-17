package com.example.spendwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spendwise.data.entity.BudgetEntity
import com.example.spendwise.data.entity.CategoryEntity
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.data.entity.UserSettingsEntity
import com.example.spendwise.model.*
import com.example.spendwise.repository.SpendWiseRepository
import com.example.spendwise.ui.components.getCategoryColor
import com.example.spendwise.util.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class SpendWiseViewModel(
    private val repository: SpendWiseRepository
) : ViewModel() {

    // 1. Core Flows from Repository
    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentTransactions: StateFlow<List<TransactionEntity>> = repository.recentTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalIncome: StateFlow<Double> = repository.totalIncome
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpenses: StateFlow<Double> = repository.totalExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val monthlyBudget: StateFlow<BudgetEntity?> = repository.getBudgetForCurrentMonth()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userSettings: StateFlow<UserSettingsEntity?> = repository.userSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val categories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 2. Computed Total Balance (Income - Expenses)
    val totalBalance: StateFlow<Double> = combine(totalIncome, totalExpenses) { inc, exp ->
        inc - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // 3. Home Screen Time Range (1W, 1M, 3M, All)
    val selectedTimeRange = MutableStateFlow(TimeRange.ONE_MONTH)
    fun setTimeRange(range: TimeRange) {
        selectedTimeRange.value = range
    }

    // 4. Search & Filter State in Activity / Transactions Screen
    val searchQuery = MutableStateFlow("")
    val selectedTypeFilter = MutableStateFlow<TransactionType?>(null) // null = ALL
    val selectedCategoryFilter = MutableStateFlow<String?>(null)    // null = ALL
    val selectedSortOrder = MutableStateFlow(SortOrder.NEWEST)

    fun setSearchQuery(query: String) { searchQuery.value = query }
    fun setTypeFilter(type: TransactionType?) { selectedTypeFilter.value = type }
    fun setCategoryFilter(category: String?) { selectedCategoryFilter.value = category }
    fun setSortOrder(order: SortOrder) { selectedSortOrder.value = order }

    val filteredTransactions: StateFlow<List<TransactionEntity>> = combine(
        allTransactions,
        searchQuery,
        selectedTypeFilter,
        selectedCategoryFilter,
        selectedSortOrder
    ) { list, query, type, cat, sort ->
        list.filter { tx ->
            val matchesQuery = query.isBlank() ||
                    tx.description.contains(query, ignoreCase = true) ||
                    tx.category.contains(query, ignoreCase = true) ||
                    tx.paymentMethod.label.contains(query, ignoreCase = true)
            val matchesType = type == null || tx.type == type
            val matchesCategory = cat == null || tx.category.equals(cat, ignoreCase = true)
            matchesQuery && matchesType && matchesCategory
        }.let { filtered ->
            when (sort) {
                SortOrder.NEWEST -> filtered.sortedWith(compareByDescending<TransactionEntity> { it.date }.thenByDescending { it.createdAt })
                SortOrder.OLDEST -> filtered.sortedWith(compareBy<TransactionEntity> { it.date }.thenBy { it.createdAt })
                SortOrder.HIGHEST -> filtered.sortedByDescending { it.amount }
                SortOrder.LOWEST -> filtered.sortedBy { it.amount }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 5. Analytics Screen State (Period: Week, Month, Year)
    val analyticsPeriod = MutableStateFlow(PeriodType.MONTH)
    fun setAnalyticsPeriod(period: PeriodType) { analyticsPeriod.value = period }

    // Filtered transactions for the selected analytics period
    private val analyticsTransactions: Flow<List<TransactionEntity>> = combine(
        allTransactions,
        analyticsPeriod
    ) { txList, period ->
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        val startTime = when (period) {
            PeriodType.WEEK -> DateUtils.getStartOfWeek(calendar)
            PeriodType.MONTH -> DateUtils.getStartOfMonth(calendar)
            PeriodType.YEAR -> {
                calendar.set(Calendar.DAY_OF_YEAR, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis
            }
        }
        txList.filter { it.date >= startTime && it.type == TransactionType.EXPENSE }
    }

    val periodExpensesTotal: StateFlow<Double> = analyticsTransactions.map { list ->
        list.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val categorySpending: StateFlow<List<CategorySpending>> = analyticsTransactions.map { list ->
        val total = list.sumOf { it.amount }
        if (total <= 0.0) return@map emptyList()

        val grouped = list.groupBy { it.category }
        val categoryColorMap = mapOf(
            "Food" to "#C1866B",
            "Transport" to "#2F3B2F",
            "Education" to "#A8B79B",
            "Shopping" to "#E0BBA8",
            "Entertainment" to "#C1866B",
            "Health" to "#A96B4E",
            "Bills" to "#A8B79B",
            "Other" to "#D6D3C6"
        )

        grouped.map { (category, txs) ->
            val sum = txs.sumOf { it.amount }
            val percentage = ((sum / total) * 100).toFloat()
            val colorHex = categoryColorMap[category] ?: "#A8B79B"
            CategorySpending(
                categoryName = category,
                totalAmount = sum,
                percentage = percentage,
                colorHex = colorHex,
                count = txs.size
            )
        }.sortedByDescending { it.totalAmount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topSpendingCategory: StateFlow<String> = categorySpending.map { list ->
        val top = list.firstOrNull()
        if (top != null) {
            "${top.categoryName} is your highest spending category (${String.format("%.0f", top.percentage)}% of total)."
        } else {
            "No spending recorded for this period yet."
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "No expenses yet.")

    // Weekly day-by-day spending bars (M, T, W, T, F, S, S)
    val weeklySpendingDays: StateFlow<List<DailySpending>> = allTransactions.map { list ->
        val calendar = Calendar.getInstance()
        val currentWeekStart = DateUtils.getStartOfWeek(calendar)
        val dayMs = 86400000L
        val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

        val days = (0 until 7).map { dayIndex ->
            val startOfDay = currentWeekStart + (dayIndex * dayMs)
            val endOfDay = startOfDay + dayMs - 1
            val daySum = list.filter {
                it.type == TransactionType.EXPENSE && it.date in startOfDay..endOfDay
            }.sumOf { it.amount }

            DailySpending(
                dayLabel = dayLabels[dayIndex],
                dateEpochDay = startOfDay,
                amount = daySum
            )
        }

        val maxVal = days.maxOfOrNull { it.amount } ?: 0.0
        days.map { it.copy(isPeak = it.amount == maxVal && maxVal > 0.0) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 6. User Actions (Mutations)
    fun addTransaction(
        type: TransactionType,
        amount: Double,
        category: String,
        description: String,
        date: Long,
        paymentMethod: PaymentMethod,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (amount <= 0.0) {
            onError("Amount must be greater than zero")
            return
        }
        if (category.isBlank()) {
            onError("Please select a category")
            return
        }
        viewModelScope.launch {
            try {
                repository.insertTransaction(
                    TransactionEntity(
                        type = type,
                        amount = amount,
                        category = category,
                        description = description.trim(),
                        date = date,
                        paymentMethod = paymentMethod
                    )
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to save transaction")
            }
        }
    }

    fun updateTransaction(
        transaction: TransactionEntity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (transaction.amount <= 0.0) {
            onError("Amount must be greater than zero")
            return
        }
        viewModelScope.launch {
            try {
                repository.updateTransaction(transaction)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to update transaction")
            }
        }
    }

    fun deleteTransaction(transaction: TransactionEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
            onComplete()
        }
    }

    fun setMonthlyBudget(amount: Double, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.setMonthlyBudget(amount)
            onComplete()
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            repository.updateDarkMode(isDark)
        }
    }

    fun updateProfile(name: String, collegeId: String) {
        viewModelScope.launch {
            val current = userSettings.value ?: UserSettingsEntity()
            repository.updateSettings(current.copy(studentName = name, collegeId = collegeId))
        }
    }

    fun clearAllData(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.clearAllData()
            onComplete()
        }
    }

    fun resetToSampleData(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.resetToSampleData()
            onComplete()
        }
    }

    fun exportDataSummary(): String {
        val txs = allTransactions.value
        val sb = StringBuilder()
        sb.append("SPENDWISE EXPENSE REPORT\n")
        sb.append("Generated on: ${DateUtils.formatDate(System.currentTimeMillis())}\n")
        sb.append("Total Income: ₹${totalIncome.value}\n")
        sb.append("Total Expenses: ₹${totalExpenses.value}\n")
        sb.append("Current Balance: ₹${totalBalance.value}\n\n")
        sb.append("TRANSACTIONS (${txs.size}):\n")
        sb.append("Date, Type, Category, Description, Method, Amount\n")
        txs.forEach { tx ->
            sb.append("${DateUtils.formatDate(tx.date)}, ${tx.type.name}, ${tx.category}, \"${tx.description}\", ${tx.paymentMethod.name}, ₹${tx.amount}\n")
        }
        return sb.toString()
    }
}

class SpendWiseViewModelFactory(
    private val repository: SpendWiseRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpendWiseViewModel::class.java)) {
            return SpendWiseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
