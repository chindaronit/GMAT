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

    val userState by userViewModel.state.collectAsState()
    val leaderboardState by leaderboardViewModel.state.collectAsState()
    val transactionState by transactionViewModel.state.collectAsState()
    val scannerState by scannerViewModel.state.collectAsState()

    NavHost(navController, startDestination = NavRoutes.Login.route) {

        animatedComposable(NavRoutes.Profile.route) {
            Profile(
                navController = navController,
                user = userState.user,
                onUserEvents = userViewModel::onEvent,
                onTransactionEvents = transactionViewModel::onEvent,
                onLeaderboardEvents = leaderboardViewModel::onEvent,
                onScannerEvents = scannerViewModel::onEvent,
                authToken = userState.authToken,
            )
        }

        animatedComposable(NavRoutes.Rewards.route) {
            Rewards(
                navController = navController,
                onLeaderboardEvents = leaderboardViewModel::onEvent,
                isLoading = (userState.isLoading || leaderboardState.isLoading),
                leaderboardEntries = leaderboardState.allEntries.data,
                user = userState.user!!,
                userLeaderboardEntry = leaderboardState.userLeaderboardEntry,
                authToken = userState.authToken,
            )
        }

        animatedComposable(NavRoutes.UpgradeQR.route) {
            UpgradeQR(
                navController = navController,
                scannedQR = scannerState.details,
                onScannerEvent = scannerViewModel::onEvent,
                onUserEvents = userViewModel::onEvent,
                authToken = userState.authToken,
            )
        }

        animatedComposable(NavRoutes.UpgradedQR.route) {
            UpgradedQR(
                navController = navController,
                isLoading = userState.isLoading,
                qrCode = userState.user!!.qr,
                vpa = userState.user!!.vpa,
                authToken = userState.authToken,
            )
        }

        animatedComposable(NavRoutes.Home.route) {
            HomeScreen(
                navController = navController,
                scannedQR = scannerState.details,
                onScannerEvent = scannerViewModel::onEvent,
                user = userState.user,
                authToken = userState.authToken,
                isLoading = userState.isLoading || transactionState.isLoading,
                recentUserTransactions = transactionState.recentUserTransactions,
                onTransactionEvents = transactionViewModel::onEvent
            )
        }

        animatedComposable(route = NavRoutes.TransactionChat.route + "/{chatIndex}",
            arguments = listOf(
                navArgument("chatIndex") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = false
                }
            )) { entry ->

            TransactionChat(
                navController = navController,
                user = userState.user,
                recentUserTransactions = transactionState.recentUserTransactions,
                chatIndex = entry.arguments?.getString("chatIndex") ?: "",
                onQRScannerEvents = scannerViewModel::onEvent
            )
        }

        animatedComposable(route = NavRoutes.TransactionReceipt.route + "/{txnId}" + "/{userId}",
            arguments = listOf(
                navArgument("txnId") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = false
                },
                navArgument("userId") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = false
                }
            )) { entry ->

            TransactionReceipt(
                navController = navController,
                isLoading = transactionState.isLoading || userState.isLoading,
                user = userState.user!!,
                transaction = transactionState.transaction,
                txnId = entry.arguments?.getString("txnId") ?: "",
                onTransactionEvents = transactionViewModel::onEvent,
                userId = entry.arguments?.getString("userId") ?: "",
                authToken = userState.authToken
            )
        }

        animatedComposable(NavRoutes.TransactionHistory.route) {
            TransactionHistory(
                navController = navController,
                isLoading = transactionState.isLoading || userState.isLoading,
                transactionHistory = transactionState.transactionHistory,
                user = userState.user!!,
                onTransactionEvents = transactionViewModel::onEvent,
                authToken = userState.authToken
            )
        }

        animatedComposable(NavRoutes.AddTransactionDetails.route) {
            AddTransactionDetails(
                navController = navController,
                scannedQR = scannerState.details,
                transaction = transactionState.transaction,
                user = userState.user!!,
                onTransactionEvents = transactionViewModel::onEvent,
                onScannerEvent = scannerViewModel::onEvent,
                onLeaderboardEvents = leaderboardViewModel::onEvent,
                authToken = userState.authToken
            )
        }

        animatedComposable(route = NavRoutes.OTP.route) {
            OTP(
                navController = navController,
                user = userState.user,
                verificationId = userState.verificationId,
                onUserEvents = userViewModel::onEvent,
                authToken = userState.authToken!!
            )
        }

        authScreens.forEach { (route, screen) ->
            if (route == NavRoutes.Login.route) {
                slideInComposable(route) {
                    screen(navController, userViewModel, userState, userState.authToken!!)
                }
            } else {
                animatedComposable(route) {
                    screen(navController, userViewModel, userState, userState.authToken!!)
                }
            }
        }

        settingScreens.forEach { (route, screen) ->
            animatedComposable(route) {
                screen(navController, userViewModel, userState, userState.authToken!!)
            }
        }
    }
}
