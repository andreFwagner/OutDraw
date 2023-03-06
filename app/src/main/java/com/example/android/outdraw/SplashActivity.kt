package com.example.android.outdraw

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            delay(1500)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }

    }
}
