package com.example.web_app_ondemande

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.web_app_ondemande.navigation.AppNavHost
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Activity Utama Aplikasi Onde Mande.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OndeMandeTheme {
                AppNavHost()
            }
        }
    }
}
