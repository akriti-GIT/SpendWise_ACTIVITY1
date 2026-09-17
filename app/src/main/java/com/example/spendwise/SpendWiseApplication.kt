package com.example.spendwise

import android.app.Application
import com.example.spendwise.data.local.AppDatabase
import com.example.spendwise.repository.SpendWiseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class SpendWiseApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { SpendWiseRepository(database) }
}
