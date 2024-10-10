package com.gmat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.gmat.ui.screen.login.Login
import com.gmat.ui.screen.login.OTP
import com.gmat.ui.screen.login.Register
import com.gmat.ui.screen.profile.AboutUs
import com.gmat.ui.screen.profile.EditProfileDetails
import com.gmat.ui.screen.profile.FAQ
import com.gmat.ui.screen.profile.Languages
import com.gmat.ui.screen.profile.Profile
import com.gmat.ui.screen.transaction.AddTransactionDetails
import com.gmat.ui.screen.transaction.TransactionReceipt
import com.gmat.ui.viewModel.UserViewModel

sealed class NavRoutes(val route: String) {
    data object Profile : NavRoutes("profile")
    data object Language : NavRoutes("profile/language")
    data object AboutUs : NavRoutes("profile/about")
    data object EditDetails : NavRoutes("profile/editDetails")
    data object FAQ : NavRoutes("profile/faq")
    data object TransactionHistory : NavRoutes("history")
    data object Rewards : NavRoutes("reward")
    data object Login : NavRoutes("login")
    data object OTP : NavRoutes("otp")
    data object Register : NavRoutes("register")
    data object AddTransactionDetails : NavRoutes("addTransactionDetails")
    data object TransactionReceipt : NavRoutes("receipt")
    data object UpgradeQR : NavRoutes("upgradeQr")
    data object UpgradedQR : NavRoutes("upgradedQr")
    data object TransactionChat : NavRoutes("transactionChat")
    data object Home : NavRoutes("home")

    fun withArgs(vararg args: String):String {
        return buildString {
            append(route)
            args.forEach { args ->
                append("/$args")
            }
        }
    }
}


val authScreens = mapOf<String, @Composable (navController: NavController, userViewModel: UserViewModel) -> Unit>(
    NavRoutes.Login.route to { navController, _ -> Login(navController = navController) },
    NavRoutes.Register.route to { navController, userViewModel -> Register(navController = navController, onUserEvents = userViewModel::onEvent) }
)


val settingScreens = mapOf<String, @Composable (navController: NavController) -> Unit>(
    NavRoutes.Language.route to { navController -> Languages(navController = navController) },
    NavRoutes.AboutUs.route to { navController -> AboutUs(navController = navController) },
    NavRoutes.EditDetails.route to { navController -> EditProfileDetails(navController = navController) },
    NavRoutes.FAQ.route to { navController -> FAQ(navController = navController) }
)