package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.auth.AuthScreen
import com.example.ui.screens.MainContainerScreen
import com.example.ui.theme.FitLifeProTheme
import com.example.viewmodel.FitLifeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: FitLifeViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsState()
            FitLifeProTheme(themeMode = themeMode) {
                AppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AppContent(viewModel: FitLifeViewModel) {
    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
            viewModel.onAuthStateChanged(auth.currentUser)
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    if (currentUser == null) {
        val userProfile by viewModel.userProfile.collectAsState()
        val isBangla = (userProfile?.language ?: "bn") == "bn"
        AuthScreen(
            onAuthSuccess = {
                val user = Firebase.auth.currentUser
                currentUser = user
                viewModel.onAuthStateChanged(user)
            },
            isBangla = isBangla
        )
    } else {
        MainContainerScreen(viewModel = viewModel)
    }
}
