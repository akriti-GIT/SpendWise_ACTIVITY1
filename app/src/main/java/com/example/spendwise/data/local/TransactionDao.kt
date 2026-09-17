package com.example.spendwise.data.local

import androidx.room.*
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY date DESC, createdAt DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY date DESC, createdAt DESC LIMIT :limit")
    fun getRecentTransactionsFlow(limit: Int = 5): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionById(id: Long): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startTime AND :endTime ORDER BY date DESC")
    fun getTransactionsBetween(startTime: Long, endTime: Long): Flow<List<TransactionEntity>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM transactions WHERE type = :type")
    fun getTotalAmountByTypeFlow(type: TransactionType): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM transactions WHERE type = 'EXPENSE' AND date BETWEEN :startTime AND :endTime")
    fun getExpensesBetweenFlow(startTime: Long, endTime: Long): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM transactions WHERE type = 'INCOME' AND date BETWEEN :startTime AND :endTime")
    fun getIncomeBetweenFlow(startTime: Long, endTime: Long): Flow<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(transactions: List<TransactionEntity>): List<Long>

    @Update
    fun update(transaction: TransactionEntity): Int

    @Delete
    fun delete(transaction: TransactionEntity): Int

    @Query("DELETE FROM transactions WHERE id = :id")
    fun deleteById(id: Long): Int

    @Query("DELETE FROM transactions")
    fun deleteAll(): Int

    @Query("SELECT COUNT(*) FROM transactions")
    fun getCount(): Int
}
