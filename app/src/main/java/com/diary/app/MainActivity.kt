package com.diary.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.diary.app.ui.theme.DiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as DiaryApplication
        setContent {
            DiaryTheme {
                DiaryNavHost(
                    repository = app.repository,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
