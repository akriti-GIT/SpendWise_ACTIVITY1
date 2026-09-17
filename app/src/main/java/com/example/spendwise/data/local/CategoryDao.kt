package com.example.spendwise.data.local

import androidx.room.*
import com.example.spendwise.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY id ASC")
    fun getAllCategoriesFlow(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<CategoryEntity>): List<Long>

    @Query("SELECT COUNT(*) FROM categories")
    fun getCount(): Int

    @Query("DELETE FROM categories")
    fun deleteAll(): Int
}
