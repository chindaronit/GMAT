package com.gmat.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gmat.R
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.Bar
import com.gmat.ui.components.login.Bottom
import com.gmat.ui.components.login.Top
import java.time.LocalTime
import androidx.compose.ui.res.stringResource

@Composable
fun HomeScreen(
    navController: NavController
) {
    val user = "Ronit Chinda"
    val isBusiness by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Bar(
                navController = navController,
                title = {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        Text(
                            greet(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            user,
                            maxLines = 1,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraLight,
                            overflow = TextOverflow.Ellipsis, fontFamily = FontFamily.Monospace
                        )
                    }

                },
                actions = {
                    IconButton(onClick = { navController.navigate(NavRoutes.Profile.route) }) {
                        AsyncImage(
                            model = R.drawable.user_icon,
                            contentDescription = "User Profile",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(), // Ensure the column fills available space
        ) {
            Top()

            HorizontalDivider(
                Modifier
                    .width(150.dp)
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.primary
            )


            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (isBusiness) {
                    MerchantFeatures(navController)
                } else {
                    PersonalFeatures(navController)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.business),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))

            // List of example user names
            val userNames = listOf(
                "Alice Johnson", "Bob Smith", "Carol Davis", "David Brown",
                "Eve Wilson", "Frank Moore", "Grace Lee", "Hank White",
                "Ivy Harris", "Jack Martin", "Ivy Harris", "Jack Martin"
            )


            LazyVerticalGrid(
                state = rememberLazyGridState(),
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .height(300.dp),
                content = {
                    items(userNames.size) { index ->
                        Card(
                            onClick = { navController.navigate(NavRoutes.TransactionChat.route) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                AsyncImage(
                                    model = R.drawable.user_icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = userNames[index],
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Bottom()
            }

        }
    }
}


@Composable
fun MerchantFeatures(
    navController: NavController
) {
    IconWithText(
        iconRes = R.drawable.scanner,
        label = stringResource(id = R.string.upgrade_qr),
        onClick = { navController.navigate(NavRoutes.UpgradeQR.route) }
    )
    Spacer(modifier = Modifier.width(30.dp))
    IconWithText(
        iconRes = R.drawable.qr,
        label = stringResource(id = R.string.upgraded_qr),
        onClick = { navController.navigate(NavRoutes.UpgradedQR.route) }
    )
    Spacer(modifier = Modifier.width(30.dp))
    IconWithText(
        iconRes = R.drawable.history,
        label = stringResource(id = R.string.history),
        onClick = { navController.navigate(NavRoutes.TransactionHistory.route) }
    )
}

@Composable
fun PersonalFeatures(
    navController: NavController
) {
    IconWithText(
        iconRes = R.drawable.scanner,
        label = stringResource(id = R.string.scan),
        onClick = { navController.navigate(NavRoutes.ScanQR.route) }
    )
    Spacer(modifier = Modifier.width(30.dp))
    IconWithText(
        iconRes = R.drawable.history,
        label = stringResource(id = R.string.history),
        onClick = { navController.navigate(NavRoutes.TransactionHistory.route) }
    )
    Spacer(modifier = Modifier.width(30.dp))
    IconWithText(
        iconRes = R.drawable.reward_icon,
        label = stringResource(id = R.string.rewards),
        onClick = { navController.navigate(NavRoutes.Rewards.route) }
    )
}

@Composable
fun greet(): String {
    val currentTime = LocalTime.now()
    val morningStart = LocalTime.of(0, 0)
    val noonStart = LocalTime.of(12, 0)
    val eveningStart = LocalTime.of(18, 0)

    return when {
        currentTime.isAfter(morningStart) && currentTime.isBefore(noonStart) -> stringResource(id = R.string.morning)
        currentTime.isAfter(noonStart) && currentTime.isBefore(eveningStart) -> stringResource(id = R.string.noon)
        else -> stringResource(id = R.string.evening)
    }
}


@Composable
fun IconWithText(iconRes: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp) // Minimal gap between icon and text
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .clip(CircleShape)
                .clickable(onClick = onClick)
                .padding(16.dp)
        ) {
            AsyncImage(
                model = iconRes,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
            )
        }
        Box(
            modifier = Modifier
                .widthIn(max = 84.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}