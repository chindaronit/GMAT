package com.gmat.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gmat.R
import com.gmat.data.model.UserModel
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.CenterBar
import com.gmat.ui.components.ProfilePreloader
import com.gmat.ui.events.LeaderboardEvents
import com.gmat.ui.events.QRScannerEvents
import com.gmat.ui.events.TransactionEvents
import com.gmat.ui.events.UserEvents
import com.gmat.ui.theme.DarkGreen

@Composable
fun Profile(
    navController: NavController,
    user: UserModel?,
    onUserEvents: (UserEvents) -> Unit,
    onTransactionEvents: (TransactionEvents) -> Unit,
    onLeaderboardEvents: (LeaderboardEvents) -> Unit,
    onScannerEvents: (QRScannerEvents) -> Unit,
    authToken: String?
) {

    Scaffold(
        topBar = {
            CenterBar(
                onClick = { navController.navigateUp() },
                title = {
                    Text(
                        text = stringResource(id = R.string.profile),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium
                    )
                })
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            if (user == null) {
                ProfilePreloader()
            }
            if (user != null) {
                ProfileCard(
                    uName = user.name,
                    uMobile = user.phNo,
                    uUpiId = user.vpa,
                    uProfile = user.profile
                )
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    SettingsBox(
                        title = stringResource(id = R.string.edit_profile),
                        iconResId = R.drawable.edit_icon,
                        onClick = { navController.navigate(NavRoutes.EditDetails.route) })

                    if (!user.isMerchant) {
                        SettingsBox(
                            title = stringResource(id = R.string.rewards),
                            iconResId = R.drawable.reward_icon,
                            onClick = { navController.navigate(NavRoutes.Rewards.route) })
                    }
                }

                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    SettingsBox(
                        title = stringResource(id = R.string.languages),
                        iconResId = R.drawable.globe_icon,
                        onClick = { navController.navigate(NavRoutes.Language.route) })
                    SettingsBox(
                        title = stringResource(id = R.string.about_us),
                        iconResId = R.drawable.information_icon,
                        onClick = { navController.navigate(NavRoutes.AboutUs.route) })
                    SettingsBox(
                        title = stringResource(id = R.string.faq),
                        iconResId = R.drawable.question_icon,
                        onClick = { navController.navigate(NavRoutes.FAQ.route) })
                }

                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    SettingsBox(
                        title = stringResource(id = R.string.sign_out),
                        iconResId = R.drawable.power_icon,
                        onClick = {
                            onUserEvents(UserEvents.SignOut)
                            onTransactionEvents(TransactionEvents.SignOut)
                            onLeaderboardEvents(LeaderboardEvents.SignOut)
                            onScannerEvents(QRScannerEvents.ClearState)
                            navController.navigate(NavRoutes.Login.route) {
                                popUpTo(NavRoutes.Home.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        })
                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    uName: String = "",
    uUpiId: String = "",
    uMobile: String = "",
    uProfile: String = ""
) {
    ElevatedCard(
        onClick = {},
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(18),
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(16.dp)),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 30.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            if (uProfile.isNotBlank()) {
                AsyncImage(
                    model = uProfile,
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.user_icon),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
            ) {
                Text(
                    text = uName,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uUpiId,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = uMobile,
                    style = MaterialTheme.typography.bodySmall,
                )

            }
        }
    }
}

@Composable
fun SettingsBox(
    title: String,
    iconResId: Int,
    onClick: () -> Unit,
    cornerRadius: RoundedCornerShape = RoundedCornerShape(32.dp),
    elevation: Dp = 12.dp
) {
    ElevatedCard(
        onClick = onClick,
        shape = cornerRadius,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = elevation),
        modifier = Modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 30.dp, end = 15.dp, bottom = 12.dp, top = 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            RenderPainter(
                iconResId = iconResId,
                circleColor = MaterialTheme.colorScheme.primary,
                iconTint = MaterialTheme.colorScheme.onPrimary,
                circleSize = 40.dp,
                iconSize = 20.dp
            )
        }
    }
}

@Composable
fun RenderPainter(
    iconResId: Int,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    iconTint: Color = MaterialTheme.colorScheme.onPrimary,
    circleSize: Dp = 40.dp,
    iconSize: Dp = 20.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(circleSize)
    ) {
        // Background Circle
        Box(
            modifier = Modifier
                .size(circleSize)
                .background(color = circleColor, shape = CircleShape)
        )

        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = iconTint
        )
    }
}