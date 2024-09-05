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

sealed class NavRoutes(val route: String) {
    data object Profile : NavRoutes("profile")
    data object Language : NavRoutes("profile/language")
    data object AboutUs : NavRoutes("profile/about")
    data object EditDetails : NavRoutes("profile/editDetails")
    data object FAQ : NavRoutes("profile/faq")
    data object TransactionHistory : NavRoutes("history")
    data object Rewards: NavRoutes("reward")
    data object Login : NavRoutes("login")
    data object OTP: NavRoutes("otp")
    data object Register: NavRoutes("register")
    data object AddTransactionDetails: NavRoutes("addTransactionDetails")
    data object TransactionReceipt: NavRoutes("receipt")
    data object UpgradeQR: NavRoutes("upgradeQr")
    data object UpgradedQR: NavRoutes("upgradedQr")
    data object TransactionChat: NavRoutes("transactionChat")
    data object Home: NavRoutes("home")
    data object ScanQR: NavRoutes("scanQr")
}

val transferScreens=mapOf<String,@Composable (navController: NavController) -> Unit>(
    NavRoutes.AddTransactionDetails.route to { navController -> AddTransactionDetails(navController=navController) },
    NavRoutes.TransactionReceipt.route to { navController -> TransactionReceipt(navController = navController) }
)

val authScreens=mapOf<String,@Composable (navController: NavController) -> Unit>(
    NavRoutes.Login.route to { navController -> Login(navController=navController) },
    NavRoutes.OTP.route to { navController -> OTP(navController = navController) },
    NavRoutes.Register.route to { navController -> Register(navController = navController) }
)

val settingScreens = mapOf<String, @Composable (navController: NavController) -> Unit>(
    NavRoutes.Profile.route to { navController -> Profile(navController) },
    NavRoutes.Language.route to { navController -> Languages(navController = navController) },
    NavRoutes.AboutUs.route to { navController -> AboutUs(navController = navController) },
    NavRoutes.EditDetails.route to { navController -> EditProfileDetails(navController = navController) },
    NavRoutes.FAQ.route to { navController -> FAQ(navController = navController) }
)