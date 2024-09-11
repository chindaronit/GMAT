package com.gmat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gmat.ui.screen.home.HomeScreen
import com.gmat.ui.screen.merchant.ScanQR
import com.gmat.ui.screen.merchant.UpgradeQR
import com.gmat.ui.screen.merchant.UpgradedQR
import com.gmat.ui.screen.profile.Profile
import com.gmat.ui.screen.rewards.Rewards
import com.gmat.ui.screen.transaction.TransactionChat
import com.gmat.ui.screen.transaction.TransactionHistory
import com.gmat.ui.viewModel.ScannerViewModel

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController(),scannerViewModel: ScannerViewModel) {

    NavHost(navController, startDestination = NavRoutes.Home.route) {
        animatedComposable(NavRoutes.Profile.route) {
            Profile(navController)
        }

        animatedComposable(NavRoutes.Rewards.route) {
            Rewards(navController)
        }

        animatedComposable(NavRoutes.UpgradeQR.route) {
            UpgradeQR(navController=navController)
        }

        animatedComposable(NavRoutes.UpgradedQR.route) {
            UpgradedQR(navController = navController)
        }

        animatedComposable(NavRoutes.Home.route) {
            HomeScreen(navController = navController)
        }

        animatedComposable(NavRoutes.TransactionChat.route) {
            TransactionChat(navController = navController)
        }

        animatedComposable(NavRoutes.TransactionHistory.route) {
            TransactionHistory(navController = navController)
        }

        animatedComposable(NavRoutes.ScanQR.route) {
            ScanQR(navController = navController,scannerViewModel)
        }


        transferScreens.forEach { (route, screen) ->
            if (route == NavRoutes.AddTransactionDetails.route) {
                slideInComposable(route) {
                    screen(navController)
                }
            } else {
                animatedComposable(route) {
                    screen(navController)
                }
            }
        }

        authScreens.forEach { (route, screen) ->
            if (route == NavRoutes.Login.route) {
                slideInComposable(route) {
                    screen(navController)
                }
            } else {
                animatedComposable(route) {
                    screen(navController)
                }
            }
        }

        settingScreens.forEach { (route, screen) ->
            if (route == NavRoutes.Profile.route) {
                slideInComposable(route) {
                    screen(navController)
                }
            } else {
                animatedComposable(route) {
                    screen(navController)
                }
            }
        }
    }
}
