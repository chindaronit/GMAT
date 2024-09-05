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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun HomeScreen(
    navController: NavController
) {
    var user = "Ronit Chinda"
    var isBusiness by remember { mutableStateOf(false) }

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
                            user ?: "",
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
                text = "Business",
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
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
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Bottom()
            }
        }

    }
}

class BottomDrawerShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val radius = size.height / 2 // Radius of the curve
            val width = size.width

            // Draw the semicircular ends
            addRoundRect(
                RoundRect(
                    rect = Rect(Offset(0f, 0f), Size(radius * 2, size.height)),
                    cornerRadius = CornerRadius(radius, radius)
                )
            )
            addRoundRect(
                RoundRect(
                    rect = Rect(Offset(width - radius * 2, 0f), Size(radius * 2, size.height)),
                    cornerRadius = CornerRadius(radius, radius)
                )
            )
            // Draw the straight lines in between
            moveTo(radius, size.height / 2)
            lineTo(width - radius, size.height / 2)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun MerchantFeatures(
    navController: NavController
) {
    IconWithText(
        iconRes = R.drawable.scanner,
        label = "Upgrade Your QR",
        onClick = { navController.navigate(NavRoutes.UpgradeQR.route) }
    )
    Spacer(modifier = Modifier.width(30.dp))
    IconWithText(
        iconRes = R.drawable.qr,
        label = "Upgraded QR",
        onClick = { navController.navigate(NavRoutes.UpgradedQR.route) }
    )
    Spacer(modifier = Modifier.width(30.dp))
    IconWithText(
        iconRes = R.drawable.history,
        label = "Transaction History",
        onClick = { navController.navigate(NavRoutes.TransactionHistory.route) }
    )
}

@Composable
fun PersonalFeatures(
    navController: NavController
) {
    IconWithText(
        iconRes = R.drawable.scanner,
        label = "Scan & Pay",
        onClick = { navController.navigate(NavRoutes.ScanQR.route) }
    )
    Spacer(modifier = Modifier.width(30.dp))
    IconWithText(
        iconRes = R.drawable.history,
        label = "Transaction History",
        onClick = { navController.navigate(NavRoutes.TransactionHistory.route) }
    )
    Spacer(modifier = Modifier.width(30.dp))
    IconWithText(
        iconRes = R.drawable.reward_icon,
        label = "Rewards",
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
        currentTime.isAfter(morningStart) && currentTime.isBefore(noonStart) -> "Good Morning,"
        currentTime.isAfter(noonStart) && currentTime.isBefore(eveningStart) -> "Good Noon,"
        else -> "Good Evening,"
    }
}

@Composable
fun UserIcon() {
    AsyncImage(
        model = R.drawable.user_icon,
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
    )
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