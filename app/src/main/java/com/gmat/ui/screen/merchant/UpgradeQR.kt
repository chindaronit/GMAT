package com.gmat.ui.screen.merchant

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.env.addGstinToUpiUrl
import com.gmat.env.extractPa
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.CenterBar
import com.gmat.ui.events.QRScannerEvents
import com.gmat.ui.events.UserEvents
import com.gmat.ui.screen.transaction.GSTVerifiedButton
import com.gmat.ui.state.QRScannerState
import com.gmat.ui.state.UserState

@Composable
fun UpgradeQR(
    modifier: Modifier = Modifier,
    navController: NavController,
    scannerState: QRScannerState,
    userState: UserState,
    onScannerEvent: (QRScannerEvents) -> Unit,
    onUserEvents: (UserEvents) -> Unit
) {
    val context = LocalContext.current
    var gstin by remember {
        mutableStateOf("")
    }
    var canContinue by remember { mutableStateOf(false) }

    BackHandler {
        onScannerEvent(QRScannerEvents.ClearState)
        navController.navigate(NavRoutes.Home.route) {
            popUpTo(NavRoutes.UpgradeQR.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }


    Scaffold(
        topBar = {
            CenterBar(
                onClick = {
                    onScannerEvent(QRScannerEvents.ClearState)
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.AddTransactionDetails.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.upgrade_qr),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        }
    ) { contentPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // QR Icon
            Icon(
                painter = painterResource(id = R.drawable.scanner), // Replace with your QR icon resource
                contentDescription = "Scanner",
                modifier = modifier
                    .size(250.dp)  // Increased size
                    .padding(top = 32.dp)
            )

            OutlinedTextField(
                value = gstin,
                onValueChange = { input ->
                    gstin = input
                    if (input.length == 15) {
                        if (input.matches(Regex("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$"))) {
                            onUserEvents(
                                UserEvents.OnChangeQR(
                                    qr = addGstinToUpiUrl(
                                        upiUrl = scannerState.details,
                                        gstin = gstin
                                    )
                                )
                            )
                            onUserEvents(UserEvents.OnChangeVPA(vpa = extractPa(scannerState.details)))
                            canContinue = true
                        } else {
                            Toast.makeText(context, "Invalid GSTIN format", Toast.LENGTH_SHORT)
                                .show()
                            canContinue = false
                        }
                    } else canContinue = false
                },
                label = { Text("GSTIN") },
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                modifier = modifier
                    .width(300.dp)  // Fixed width
                    .padding(horizontal = 40.dp)
            )
            if (canContinue)
                GSTVerifiedButton()
            Spacer(modifier = modifier.weight(1f))
            Button(
                onClick = {
                    onScannerEvent(QRScannerEvents.ClearState)
                    onUserEvents(UserEvents.UpdateUser)
                    navController.navigate(NavRoutes.UpgradedQR.route)
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = canContinue

            ) {
                Text(stringResource(id = R.string.upgrade_qr))
            }
        }
    }
}