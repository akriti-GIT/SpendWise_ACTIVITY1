package com.example.spendwise.repository

import com.example.spendwise.data.entity.BudgetEntity
import com.example.spendwise.data.entity.CategoryEntity
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.data.entity.UserSettingsEntity
import com.example.spendwise.data.local.AppDatabase
import com.example.spendwise.model.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Calendar

class SpendWiseRepository(private val database: AppDatabase) {

    private val transactionDao = database.transactionDao()
    private val budgetDao = database.budgetDao()
    private val categoryDao = database.categoryDao()
    private val userSettingsDao = database.userSettingsDao()

    // Transactions
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactionsFlow()
    val recentTransactions: Flow<List<TransactionEntity>> = transactionDao.getRecentTransactionsFlow(5)
    val totalIncome: Flow<Double> = transactionDao.getTotalAmountByTypeFlow(TransactionType.INCOME)
    val totalExpenses: Flow<Double> = transactionDao.getTotalAmountByTypeFlow(TransactionType.EXPENSE)

    suspend fun getTransactionById(id: Long): TransactionEntity? = withContext(Dispatchers.IO) {
        transactionDao.getTransactionById(id)
    }

    suspend fun insertTransaction(transaction: TransactionEntity): Long = withContext(Dispatchers.IO) {
        transactionDao.insert(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) = withContext(Dispatchers.IO) {
        transactionDao.update(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) = withContext(Dispatchers.IO) {
        transactionDao.delete(transaction)
    }

    suspend fun deleteTransactionById(id: Long) = withContext(Dispatchers.IO) {
        transactionDao.deleteById(id)
    }

    fun getTransactionsBetween(startTime: Long, endTime: Long): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsBetween(startTime, endTime)

    fun getExpensesBetween(startTime: Long, endTime: Long): Flow<Double> =
        transactionDao.getExpensesBetweenFlow(startTime, endTime)

    fun getIncomeBetween(startTime: Long, endTime: Long): Flow<Double> =
        transactionDao.getIncomeBetweenFlow(startTime, endTime)

    // Budgets
    fun getBudgetForCurrentMonth(): Flow<BudgetEntity?> {
        val calendar = Calendar.getInstance()
        return budgetDao.getBudgetForMonthFlow(
            month = calendar.get(Calendar.MONTH) + 1,
            year = calendar.get(Calendar.YEAR)
        )
    }

    suspend fun setMonthlyBudget(amount: Double, categoryBudgetsJson: String = "") = withContext(Dispatchers.IO) {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        val existing = budgetDao.getBudgetForMonth(currentMonth, currentYear)
        val updated = existing?.copy(
            amount = amount,
            categoryBudgetsJson = if (categoryBudgetsJson.isNotEmpty()) categoryBudgetsJson else existing.categoryBudgetsJson
        ) ?: BudgetEntity(
            month = currentMonth,
            year = currentYear,
            amount = amount,
            categoryBudgetsJson = categoryBudgetsJson
        )
        budgetDao.insertOrUpdate(updated)

        // Also sync with UserSettings
        val settings = userSettingsDao.getSettings() ?: UserSettingsEntity()
        userSettingsDao.insertOrUpdate(settings.copy(monthlyBudget = amount))
    }

    // Categories
    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategoriesFlow()

    // Settings
    val userSettings: Flow<UserSettingsEntity?> = userSettingsDao.getSettingsFlow()

    suspend fun updateDarkMode(isDark: Boolean) = withContext(Dispatchers.IO) {
        val current = userSettingsDao.getSettings() ?: UserSettingsEntity()
        userSettingsDao.insertOrUpdate(current.copy(darkMode = isDark))
    }

    suspend fun updateSettings(settings: UserSettingsEntity) = withContext(Dispatchers.IO) {
        userSettingsDao.insertOrUpdate(settings)
    }

    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        transactionDao.deleteAll()
        budgetDao.deleteAll()
        categoryDao.deleteAll()
    }

    suspend fun resetToSampleData() = withContext(Dispatchers.IO) {
        clearAllData()
        AppDatabase.populateInitialData(database)
    }
}
