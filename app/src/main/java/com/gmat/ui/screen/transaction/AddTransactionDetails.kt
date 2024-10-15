package com.gmat.ui.screen.transaction

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.data.model.TransactionModel
import com.gmat.data.model.UserModel
import com.gmat.env.GST_REGEX
import com.gmat.env.extractPa
import com.gmat.env.extractPn
import com.gmat.env.isMerchantUpi
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.CenterBar
import com.gmat.ui.components.transaction.ProfileTransactionCard
import com.gmat.ui.events.LeaderboardEvents
import com.gmat.ui.events.QRScannerEvents
import com.gmat.ui.events.TransactionEvents
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDetails(
    modifier: Modifier = Modifier,
    navController: NavController,
    scannedQR: String,
    user: UserModel,
    transaction: TransactionModel?,
    onLeaderboardEvents: (LeaderboardEvents) -> Unit,
    onScannerEvent: (QRScannerEvents) -> Unit,
    onTransactionEvents: (TransactionEvents) -> Unit
) {

    var amount by remember { mutableStateOf("") }
    var selectedUpiId by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Merchant") }
    val context = LocalContext.current
    var gstin by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var canContinuePayment by remember {
        mutableStateOf(false)
    }
    val isMerchant = isMerchantUpi(scannedQR)
    var userConfirmation by remember {
        mutableIntStateOf(0)
    }

    if (!isMerchant) {
        LaunchedEffect(key1 = gstin, key2 = amount) {
            if (userConfirmation == 1) {
                gstin = ""
                canContinuePayment = true
            } else {
                canContinuePayment =
                    gstin.isNotBlank() && amount.isNotBlank() && amount.toFloat() > 0
            }
        }
    }

    if (isMerchant) {
        LaunchedEffect(key1 = amount) {
            canContinuePayment = amount.isNotBlank() && amount.toFloat() > 0
        }
    }

    BackHandler {
        onScannerEvent(QRScannerEvents.ClearState)
        navController.navigate(NavRoutes.Home.route) {
            popUpTo(NavRoutes.AddTransactionDetails.route) {
                inclusive = true
            } // Clears the back stack
            launchSingleTop = true  // Avoids multiple instances of the screen
        }
    }

    LaunchedEffect(key1 = transaction) {
        if (transaction != null) {
            onTransactionEvents(TransactionEvents.GetRecentTransactions(vpa = user.vpa, userId = user.userId))
            onLeaderboardEvents(
                LeaderboardEvents.AddUserTransactionRewards(
                    transactionAmount = amount,
                    userId = user.userId
                )
            )
            navController.navigate(
                NavRoutes.TransactionReceipt.withArgs(
                    transaction.txnId,
                    user.userId
                )
            ) {
                popUpTo(NavRoutes.AddTransactionDetails.route) { inclusive = true }
                launchSingleTop = true
            }
            onScannerEvent(QRScannerEvents.ClearState)
        }
    }
    if (scannedQR.isNotBlank()) {
        Scaffold(
            topBar = {
                CenterBar(
                    onClick = {
                        onScannerEvent(QRScannerEvents.ClearState)
                        navController.navigate(NavRoutes.Home.route) {
                            popUpTo(NavRoutes.AddTransactionDetails.route) {
                                inclusive = true
                            } // Clears the back stack
                            launchSingleTop = true  // Avoids multiple instances of the screen
                        }
                    },
                    actions = {},
                    title = {
                        Text("Enter Details")
                    }
                )
            },

            floatingActionButton = {
                if (canContinuePayment) {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = {
                            scope.launch {
                                showBottomSheet = true
                                sheetState.show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            }

        ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileTransactionCard(
                    uName = extractPn(scannedQR),
                    uUpiId = extractPa(scannedQR),
                    isMerchant = false
                )

                if (!isMerchant) {
                    GSTVerifiedButton()
                    MerchantPaymentDetails(
                        userConfirmation = userConfirmation,
                        onGSTChange = {
                            gstin = it
                        },
                        onDropDownSelection = {
                            userConfirmation = if (it == "Merchant") 0 else 1
                            selectedOption = it
                        }
                    )
                }
                if (!isMerchant) Spacer(modifier = modifier.height(50.dp))
                OutlinedTextField(
                    placeholder = { Text("Enter Amount") },
                    value = amount,
                    onValueChange = { input ->
                        if (input.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
                            if (input.isNotEmpty() && input.toDouble() > 100000) {
                                Toast.makeText(
                                    context,
                                    "Money should not exceed 1,00,000",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (input.length < 7) {
                                amount = input
                            }
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.CurrencyRupee,
                            contentDescription = "Currency Rupee",
                            modifier = modifier.size(24.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = modifier
                        .width(300.dp)
                        .padding(vertical = 10.dp)
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                        showBottomSheet = false
                    }
                },
                sheetState = sheetState
            ) {
                BottomSheetContent(
                    upiId = user.vpa,
                    selectedUpiId = selectedUpiId,
                    onUpiIdSelected = { id ->
                        selectedUpiId = id
                    },
                    onPayClick = {
                        scope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                        }
                        if (user.isMerchant) {
                            onTransactionEvents(
                                TransactionEvents.AddTransaction(
                                    userId = user.userId,
                                    transaction = TransactionModel(
                                        name = extractPn(scannedQR),
                                        gstin = gstin,
                                        payerId = user.vpa,
                                        payeeId = extractPa(scannedQR),
                                        amount = amount,
                                        type = 0
                                    )
                                )
                            )
                        } else {
                            if (userConfirmation == 0) {
                                onTransactionEvents(
                                    TransactionEvents.AddTransaction(
                                        userId = user.userId,
                                        transaction = TransactionModel(
                                            name = extractPn(scannedQR),
                                            gstin = gstin,
                                            payerId = user.vpa,
                                            payeeId = extractPa(scannedQR),
                                            amount = amount,
                                            type = 0
                                        )
                                    )
                                )
                            } else {
                                onTransactionEvents(
                                    TransactionEvents.AddTransaction(
                                        userId = user.userId,
                                        transaction = TransactionModel(
                                            name = extractPn(scannedQR),
                                            payerId = user.vpa,
                                            payeeId = extractPa(scannedQR),
                                            amount = amount,
                                            type = 1
                                        )
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MerchantPaymentDetails(
    onDropDownSelection: (String) -> Unit,
    onGSTChange: (String) -> Unit,
    userConfirmation: Int
) {
    val options = listOf("Merchant", "Personal")
    var gstin by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("Merchant") }

    Box(
        modifier = Modifier
            .width(300.dp)
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text("Type of Transaction") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier.width(300.dp),
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onDropDownSelection(option)
                    },
                    text = {
                        Text(text = option)
                    }
                )
            }
        }
    }
    if (userConfirmation == 0) {
        OutlinedTextField(
            value = gstin,
            onValueChange = { input ->
                gstin = input
                onGSTChange("")
                if (input.length == 15) {
                    if (input.matches(Regex(GST_REGEX))) {
                        gstin = input
                        onGSTChange(input)
                    } else {
                        Toast.makeText(context, "Invalid GSTIN format", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            label = { Text("GSTIN") },
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .width(300.dp)
                .padding(vertical = 10.dp)
        )
    }
}

@Composable
fun GSTVerifiedButton() {
    AssistChip(
        onClick = { },
        label = { Text("GST Verified") },
        leadingIcon = {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                Modifier.size(AssistChipDefaults.IconSize)
            )
        },
        modifier = Modifier.padding(vertical = 15.dp)
    )
}

@Composable
fun BottomSheetContent(
    upiId: String,
    selectedUpiId: String,
    onUpiIdSelected: (String) -> Unit,
    onPayClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Select UPI ID",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onUpiIdSelected(upiId) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = upiId,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp) // Left padding
            )
            RadioButton(
                selected = selectedUpiId == upiId,
                onClick = { onUpiIdSelected(upiId) }
            )
        }

        Button(
            onClick = onPayClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = "Pay", fontSize = 18.sp, modifier = Modifier.padding(4.dp))
        }
    }
}