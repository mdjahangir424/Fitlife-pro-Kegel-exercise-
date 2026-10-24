package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class FitLifeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initFirebaseSafely()
    }

    private fun initFirebaseSafely() {
        if (FirebaseApp.getApps(this).isEmpty()) {
            try {
                FirebaseApp.initializeApp(
                    this,
                    FirebaseOptions.Builder()
                        .setApplicationId("com.aistudio.fitlifepro.bdflp")
                        .setProjectId("ai-studio-fitlifepro")
                        .setApiKey("AIzaSyFakeKeyForLocalInitialization000")
                        .build()
                )
                Log.d("FitLifeApplication", "FirebaseApp initialized with fallback options")
            } catch (e: Exception) {
                Log.w("FitLifeApplication", "Fallback FirebaseApp initialization caught: ${e.message}")
            }
        }
    }
}
