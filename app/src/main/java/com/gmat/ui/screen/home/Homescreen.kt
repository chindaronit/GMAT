package com.gmat.ui.screen.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gmat.R
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.Bar
import com.gmat.ui.components.login.Top
import java.time.LocalTime
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.gmat.data.model.UserModel
import com.gmat.env.ChatDetails
import com.gmat.ui.components.HomeScreenPreloader
import com.gmat.ui.components.RecentTransactionPreloader
import com.gmat.ui.events.QRScannerEvents
import com.gmat.ui.events.TransactionEvents

@Composable
fun HomeScreen(
    navController: NavController,
    scannedQR: String,
    user: UserModel?,
    isLoading: Boolean,
    recentUserTransactions: List<ChatDetails>?=null,
    onTransactionEvents: (TransactionEvents) -> Unit,
    onScannerEvent: (QRScannerEvents) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    LaunchedEffect(key1 = user) {
        if (user == null) {
            navController.navigate(NavRoutes.Login.route)
        }
    }

    LaunchedEffect(key1 = recentUserTransactions) {
        if (recentUserTransactions == null && user != null) {
            if (user.isMerchant) {
                onTransactionEvents(TransactionEvents.GetRecentTransactions(null, user.vpa))
            } else {
                onTransactionEvents(TransactionEvents.GetRecentTransactions(user.userId, null))
            }
        }
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                hasCameraPermission = granted
            }
        )

    // Only request permission if it hasn’t been granted yet
    if (!hasCameraPermission) {
        LaunchedEffect(key1 = true) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(key1 = scannedQR) {
        if (scannedQR.isNotBlank()) {
            if (user!!.isMerchant) {
                navController.navigate(NavRoutes.UpgradeQR.route)
            } else {
                navController.navigate(NavRoutes.AddTransactionDetails.route)
            }
        }
    }

    if (isLoading) {
        HomeScreenPreloader()
    }

    if (user != null) {
        Scaffold(
            topBar = {
                Bar(
                    title = {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            Text(
                                greet(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                user.name,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodySmall,
                                overflow = TextOverflow.Ellipsis, fontFamily = FontFamily.Monospace
                            )
                        }

                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(NavRoutes.Profile.route) }) {
                            if (user.profile.isNotBlank()) {
                                AsyncImage(
                                    model = user.profile,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.user_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
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
                    if (user.isMerchant) {
                        MerchantFeatures(
                            navController,
                            user = user,
                            activity = activity,
                            onScanClick = {
                                onScannerEvent(QRScannerEvents.StartScanning)
                            })
                    } else {
                        PersonalFeatures(navController, onScanClick = {
                            onScannerEvent(QRScannerEvents.StartScanning)
                        })
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))

                if (!user.isMerchant || user.qr.isNotBlank()) {
                    Text(
                        text = if (!user.isMerchant) stringResource(id = R.string.business) else stringResource(
                            id = R.string.people
                        ),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    if(recentUserTransactions==null){
                        RecentTransactionPreloader()
                    }

                    if (recentUserTransactions != null) {
                        val gridSize = minOf(recentUserTransactions.size, 16) // Maximum 16 items (4x4 grid)

                        // Calculate the number of rows needed
                        val rows = if (gridSize % 4 == 0) gridSize / 4 else gridSize / 4 + 1
                        val rowHeight = 75.dp // Set the height for each row (can be adjusted as needed)

                        // Calculate the total height dynamically based on the number of rows
                        val totalHeight = rowHeight * rows

                        LazyVerticalGrid(
                            state = rememberLazyGridState(),
                            columns = GridCells.Fixed(4), // Fixed 4 columns
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .height(totalHeight) // Dynamically set the height based on rows
                                .fillMaxWidth(),
                            content = {
                                items(gridSize) { index ->
                                    val transactionUser =
                                        recentUserTransactions[index].userDetails!!
                                    val chatIndex = index.toString()

                                    Card(
                                        onClick = {
                                            navController.navigate(
                                                NavRoutes.TransactionChat.withArgs(chatIndex)
                                            )
                                        },
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
                                            if (transactionUser.profile.isNotBlank()) {
                                                AsyncImage(
                                                    model = transactionUser.profile,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .clip(CircleShape)
                                                )
                                            } else {
                                                AsyncImage(
                                                    model = R.drawable.user_icon,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(48.dp),
                                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                text = transactionUser.name,
                                                style = MaterialTheme.typography.bodyMedium,
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
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = R.drawable.gmatlogo,
                        contentDescription = null,
                        modifier = Modifier.alpha(0.8f),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
    }
}


@Composable
fun MerchantFeatures(
    navController: NavController,
    user: UserModel,
    activity: Activity,
    onScanClick: () -> Unit
) {

    IconWithText(
        iconRes = R.drawable.scanner,
        label = stringResource(id = R.string.upgrade_qr),
        onClick = {
            if (user.qr.isBlank()) {
                onScanClick()
            } else {
                Toast.makeText(activity, "QR is already Upgraded!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )
    Spacer(modifier = Modifier.width(30.dp))
    IconWithText(
        iconRes = R.drawable.qr,
        label = stringResource(id = R.string.upgraded_qr),
        onClick = {
            if (user.qr.isNotBlank()) {
                navController.navigate(NavRoutes.UpgradedQR.route)
            } else {
                Toast.makeText(activity, "Upgrade QR first!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )
    Spacer(modifier = Modifier.width(30.dp))
    IconWithText(
        iconRes = R.drawable.history,
        label = stringResource(id = R.string.history),
        onClick = {
            if (user.qr.isNotBlank()) {
                navController.navigate(NavRoutes.TransactionHistory.route)
            } else {
                Toast.makeText(activity, "Upgrade QR first!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )
}

@Composable
fun PersonalFeatures(
    navController: NavController,
    onScanClick: () -> Unit
) {
    IconWithText(
        iconRes = R.drawable.scanner,
        label = stringResource(id = R.string.scan),
        onClick = { onScanClick() }
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
        currentTime.isAfter(morningStart) && currentTime.isBefore(noonStart) -> stringResource(
            id = R.string.morning
        )

        currentTime.isAfter(noonStart) && currentTime.isBefore(eveningStart) -> stringResource(
            id = R.string.noon
        )

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
                style = MaterialTheme.typography.labelMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}