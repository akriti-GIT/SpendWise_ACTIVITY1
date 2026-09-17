package com.example.spendwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.spendwise.ui.navigation.SpendWiseNavGraph
import com.example.spendwise.ui.theme.SpendWiseTheme
import com.example.spendwise.viewmodel.SpendWiseViewModel
import com.example.spendwise.viewmodel.SpendWiseViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: SpendWiseViewModel by viewModels {
        val app = application as SpendWiseApplication
        SpendWiseViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userSettings by viewModel.userSettings.collectAsState()
            val isDark = userSettings?.darkMode ?: isSystemInDarkTheme()

            SpendWiseTheme(darkTheme = isDark) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SpendWiseNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
