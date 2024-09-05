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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.CenterBar

@Composable
fun Profile(
    navController: NavController
) {

    Scaffold(
        topBar = {
            CenterBar(
                navController = navController,
                title = {
                    Text(
                        text = stringResource(id = R.string.profile),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
            ProfileCard(uName = "Ronit Chinda", uMobile = "7988224882", uUpiId = "7988224882@sbi")
            Column(
                modifier = Modifier.padding(20.dp),
            ) {
                SettingsBox(
                    title = stringResource(id = R.string.edit_profile),
                    iconResId = R.drawable.edit_icon,
                    onClick = { navController.navigate(NavRoutes.EditDetails.route) })
                SettingsBox(
                    title = stringResource(id = R.string.rewards),
                    iconResId = R.drawable.reward_icon,
                    onClick = { navController.navigate(NavRoutes.Rewards.route) })
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
                    onClick = {})
            }
        }
    }
}

@Composable
fun ProfileCard(
    uName: String = "",
    uUpiId: String = "",
    uMobile: String = "",
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
            Icon(
                painter = painterResource(R.drawable.user_icon),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = uName,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
                Text(text = uUpiId)
                Text(
                    text = uMobile,
                    fontWeight = FontWeight.SemiBold
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
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    elevation: Dp = 6.dp
) {
    ElevatedCard(
        onClick = onClick,
        shape = cornerRadius,
        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = elevation),
        modifier = Modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
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
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = FontFamily.Serif
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