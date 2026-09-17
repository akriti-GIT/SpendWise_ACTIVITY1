package com.example.spendwise.data.local

import androidx.room.*
import com.example.spendwise.data.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year LIMIT 1")
    fun getBudgetForMonthFlow(month: Int, year: Int): Flow<BudgetEntity?>

    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year LIMIT 1")
    fun getBudgetForMonth(month: Int, year: Int): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(budget: BudgetEntity): Long

    @Query("DELETE FROM budgets")
    fun deleteAll(): Int
}
