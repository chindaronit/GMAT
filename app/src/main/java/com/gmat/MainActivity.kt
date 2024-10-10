package com.gmat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmat.navigation.AppNavHost
import com.gmat.ui.events.UserEvents
import com.gmat.ui.theme.GMATTheme
import com.gmat.ui.viewModel.LeaderboardViewModel
import com.gmat.ui.viewModel.ScannerViewModel
import com.gmat.ui.viewModel.TransactionViewModel
import com.gmat.ui.viewModel.UserViewModel
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installGoogleScanner()
        setContent {
            GMATTheme {
                val scannerViewModel: ScannerViewModel = hiltViewModel()
                val userViewModel: UserViewModel = hiltViewModel()
                val leaderboardViewModel: LeaderboardViewModel = hiltViewModel()
                val transactionViewModel: TransactionViewModel = hiltViewModel()
                AppNavHost(scannerViewModel=scannerViewModel, userViewModel = userViewModel, transactionViewModel = transactionViewModel, leaderboardViewModel = leaderboardViewModel)
            }
        }
    }

    private fun installGoogleScanner() {
        val moduleInstall = ModuleInstall.getClient(this)
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(GmsBarcodeScanning.getClient(this))
            .build()

        moduleInstall.installModules(moduleInstallRequest).addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}
