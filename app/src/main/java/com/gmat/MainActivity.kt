package com.gmat

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.gmat.navigation.AppNavHost
import com.gmat.ui.theme.GMATTheme

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GMATTheme {
                AppNavHost()
            }
        }
    }
}

