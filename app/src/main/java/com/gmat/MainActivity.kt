package com.gmat

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmat.navigation.AppNavHost
import com.gmat.ui.theme.GMATTheme
import com.gmat.ui.viewModel.ScannerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GMATTheme {
                val scannerViewModel: ScannerViewModel = hiltViewModel()

                AppNavHost(scannerViewModel=scannerViewModel)
            }
        }
    }
}

