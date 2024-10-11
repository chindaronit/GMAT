package com.gmat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gmat.ui.screen.home.HomeScreen
import com.gmat.ui.screen.login.OTP
import com.gmat.ui.screen.merchant.UpgradeQR
import com.gmat.ui.screen.merchant.UpgradedQR
import com.gmat.ui.screen.profile.Profile
import com.gmat.ui.screen.rewards.Rewards
import com.gmat.ui.screen.transaction.AddTransactionDetails
import com.gmat.ui.screen.transaction.TransactionChat
import com.gmat.ui.screen.transaction.TransactionHistory
import com.gmat.ui.screen.transaction.TransactionReceipt
import com.gmat.ui.viewModel.LeaderboardViewModel
import com.gmat.ui.viewModel.ScannerViewModel
import com.gmat.ui.viewModel.TransactionViewModel
import com.gmat.ui.viewModel.UserViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    scannerViewModel: ScannerViewModel,
    userViewModel: UserViewModel,
    transactionViewModel: TransactionViewModel,
    leaderboardViewModel: LeaderboardViewModel
) {

    NavHost(navController, startDestination = NavRoutes.Login.route) {
        animatedComposable(NavRoutes.Profile.route) {
            val userState by userViewModel.state.collectAsState()
            Profile(navController, userState)
        }

        animatedComposable(NavRoutes.Rewards.route) {
            val leaderboardState by leaderboardViewModel.state.collectAsState()
            val userState by userViewModel.state.collectAsState()
            Rewards(navController, leaderboardState = leaderboardState, onLeaderboardEvents = leaderboardViewModel::onEvent,userState=userState)
        }

        animatedComposable(NavRoutes.UpgradeQR.route) {
            val state by scannerViewModel.state.collectAsState()
            UpgradeQR(
                navController = navController,
                scannerState = state,
                onScannerEvent = scannerViewModel::onEvent
            )
        }

        animatedComposable(NavRoutes.UpgradedQR.route) {
            val userState by userViewModel.state.collectAsState()
            UpgradedQR(navController = navController, userState = userState)
        }

        animatedComposable(NavRoutes.Home.route) {
            val scannerState by scannerViewModel.state.collectAsState()
            val userState by userViewModel.state.collectAsState()
            HomeScreen(
                navController = navController,
                scannerState = scannerState,
                onScannerEvent = scannerViewModel::onEvent,
                userState = userState,
                onUserEvents = userViewModel::onEvent
            )
        }

        animatedComposable(NavRoutes.TransactionChat.route) {
            val userState by userViewModel.state.collectAsState()
            val transactionState by transactionViewModel.state.collectAsState()
            TransactionChat(navController = navController, userState = userState, transactionState = transactionState)
        }

        animatedComposable(NavRoutes.TransactionHistory.route) {
            val transactionState by transactionViewModel.state.collectAsState()
            TransactionHistory(navController = navController, transactionState = transactionState)
        }

        animatedComposable(NavRoutes.AddTransactionDetails.route) {
            val state by scannerViewModel.state.collectAsState()
            AddTransactionDetails(
                navController = navController,
                scannerState = state,
                onScannerEvent = scannerViewModel::onEvent
            )
        }

        animatedComposable(NavRoutes.TransactionReceipt.route) {
            val transactionState by transactionViewModel.state.collectAsState()
            TransactionReceipt(navController = navController,transactionState = transactionState)
        }

        animatedComposable(route = NavRoutes.OTP.route) {
            val userState by userViewModel.state.collectAsState()
            OTP(
                navController = navController,
                userState = userState,
                onUserEvents = userViewModel::onEvent
            )
        }

        authScreens.forEach { (route, screen) ->
            if (route == NavRoutes.Login.route) {
                slideInComposable(route) {
                    val userState by userViewModel.state.collectAsState()
                    screen(navController,userViewModel,userState)
                }
            } else {
                animatedComposable(route) {
                    val userState by userViewModel.state.collectAsState()
                    screen(navController,userViewModel,userState)
                }
            }
        }

        settingScreens.forEach { (route, screen) ->
            animatedComposable(route) {
                val userState by userViewModel.state.collectAsState()
                screen(navController,userViewModel,userState)
            }
        }
    }
}
