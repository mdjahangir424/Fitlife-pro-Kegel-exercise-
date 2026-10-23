package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.MainContainerScreen
import com.example.ui.theme.FitLifeProTheme
import com.example.viewmodel.FitLifeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: FitLifeViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsState()
            FitLifeProTheme(themeMode = themeMode) {
                MainContainerScreen(viewModel = viewModel)
            }
        }
    }
}
