package com.example.spendwise.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.spendwise.data.entity.BudgetEntity
import com.example.spendwise.data.entity.CategoryEntity
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.data.entity.UserSettingsEntity
import com.example.spendwise.model.PaymentMethod
import com.example.spendwise.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Database(
    entities = [
        TransactionEntity::class,
        BudgetEntity::class,
        CategoryEntity::class,
        UserSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spendwise_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        fun populateInitialData(database: AppDatabase) {
            val categoryDao = database.categoryDao()
            val transactionDao = database.transactionDao()
            val budgetDao = database.budgetDao()
            val userSettingsDao = database.userSettingsDao()

            // 1. Seed Categories
            val defaultCategories = listOf(
                CategoryEntity(name = "Food", iconName = "restaurant", colorHex = "#C1866B", type = TransactionType.EXPENSE),
                CategoryEntity(name = "Transport", iconName = "directions_bus", colorHex = "#A8B79B", type = TransactionType.EXPENSE),
                CategoryEntity(name = "Education", iconName = "menu_book", colorHex = "#2F3B2F", type = TransactionType.EXPENSE),
                CategoryEntity(name = "Shopping", iconName = "shopping_bag", colorHex = "#E0BBA8", type = TransactionType.EXPENSE),
                CategoryEntity(name = "Entertainment", iconName = "theater_comedy", colorHex = "#C1866B", type = TransactionType.EXPENSE),
                CategoryEntity(name = "Health", iconName = "favorite", colorHex = "#A96B4E", type = TransactionType.EXPENSE),
                CategoryEntity(name = "Bills", iconName = "receipt_long", colorHex = "#A8B79B", type = TransactionType.EXPENSE),
                CategoryEntity(name = "Other", iconName = "more_horiz", colorHex = "#D6D3C6", type = TransactionType.EXPENSE),
                CategoryEntity(name = "Salary", iconName = "payments", colorHex = "#4A6B4A", type = TransactionType.INCOME),
                CategoryEntity(name = "Scholarship", iconName = "school", colorHex = "#2F3B2F", type = TransactionType.INCOME),
                CategoryEntity(name = "Allowance", iconName = "savings", colorHex = "#C6D4BC", type = TransactionType.INCOME)
            )
            categoryDao.insertAll(defaultCategories)

            // 2. Seed Default Settings
            userSettingsDao.insertOrUpdate(
                UserSettingsEntity(
                    id = 1,
                    currency = "₹",
                    darkMode = false,
                    notificationsEnabled = true,
                    studentName = "Maya Chen",
                    collegeId = "MCA-2026-087",
                    monthlyBudget = 10000.0
                )
            )

            // 3. Seed Monthly Budget for current month
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH) + 1
            val currentYear = calendar.get(Calendar.YEAR)
            budgetDao.insertOrUpdate(
                BudgetEntity(
                    month = currentMonth,
                    year = currentYear,
                    amount = 10000.0,
                    categoryBudgetsJson = "{\"Food\":3000.0,\"Transport\":2000.0,\"Education\":2000.0,\"Shopping\":1500.0,\"Entertainment\":1000.0,\"Other\":500.0}"
                )
            )

            // 4. Seed Realistic Transactions (Income: ₹20,000, Expenses: ₹7,550, Balance: ₹12,450)
            val now = System.currentTimeMillis()
            val dayMs = 86400000L

            val sampleTransactions = listOf(
                // Incomes
                TransactionEntity(
                    type = TransactionType.INCOME,
                    amount = 10000.0,
                    category = "Scholarship",
                    description = "Semester Merit Scholarship",
                    date = now - (dayMs * 5),
                    paymentMethod = PaymentMethod.UPI
                ),
                TransactionEntity(
                    type = TransactionType.INCOME,
                    amount = 10000.0,
                    category = "Salary",
                    description = "Part-time Campus Assistant shift",
                    date = now - (dayMs * 2),
                    paymentMethod = PaymentMethod.UPI
                ),
                // Expenses (Total: 2650 + 1500 + 1200 + 1350 + 500 + 350 = 7,550)
                TransactionEntity(
                    type = TransactionType.EXPENSE,
                    amount = 250.0,
                    category = "Food",
                    description = "Campus Cafeteria Lunch",
                    date = now - (3600000L * 2), // Today
                    paymentMethod = PaymentMethod.UPI
                ),
                TransactionEntity(
                    type = TransactionType.EXPENSE,
                    amount = 80.0,
                    category = "Transport",
                    description = "Metro Recharge",
                    date = now - (3600000L * 4), // Today
                    paymentMethod = PaymentMethod.CARD
                ),
                TransactionEntity(
                    type = TransactionType.EXPENSE,
                    amount = 500.0,
                    category = "Education",
                    description = "Algorithms & Data Structures Textbook",
                    date = now - dayMs, // Yesterday
                    paymentMethod = PaymentMethod.CARD
                ),
                TransactionEntity(
                    type = TransactionType.EXPENSE,
                    amount = 2400.0,
                    category = "Food",
                    description = "Monthly Mess Subscription",
                    date = now - (dayMs * 3),
                    paymentMethod = PaymentMethod.UPI
                ),
                TransactionEntity(
                    type = TransactionType.EXPENSE,
                    amount = 1420.0,
                    category = "Transport",
                    description = "Monthly Bus Pass",
                    date = now - (dayMs * 4),
                    paymentMethod = PaymentMethod.UPI
                ),
                TransactionEntity(
                    type = TransactionType.EXPENSE,
                    amount = 700.0,
                    category = "Education",
                    description = "Lab Notebooks & Stationery",
                    date = now - (dayMs * 6),
                    paymentMethod = PaymentMethod.CASH
                ),
                TransactionEntity(
                    type = TransactionType.EXPENSE,
                    amount = 1350.0,
                    category = "Shopping",
                    description = "College Backpack & Umbrella",
                    date = now - (dayMs * 7),
                    paymentMethod = PaymentMethod.CARD
                ),
                TransactionEntity(
                    type = TransactionType.EXPENSE,
                    amount = 500.0,
                    category = "Entertainment",
                    description = "Weekend Movie & Snacks",
                    date = now - (dayMs * 8),
                    paymentMethod = PaymentMethod.UPI
                ),
                TransactionEntity(
                    type = TransactionType.EXPENSE,
                    amount = 350.0,
                    category = "Other",
                    description = "Mobile Data Recharge",
                    date = now - (dayMs * 9),
                    paymentMethod = PaymentMethod.UPI
                )
            )
            transactionDao.insertAll(sampleTransactions)
        }
    }
}
