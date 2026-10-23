package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.MainContainerScreen
import com.example.ui.theme.FitLifeProTheme
import com.example.viewmodel.FitLifeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitLifeProTheme {
                val viewModel: FitLifeViewModel = viewModel()
                MainContainerScreen(viewModel = viewModel)
            }
        }
    }
}
