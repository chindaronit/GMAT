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
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.CenterBar
import com.gmat.ui.components.transaction.ProfileTransactionCard
import com.gmat.ui.events.QRScannerEvents
import com.gmat.ui.state.QRScannerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDetails(
    modifier: Modifier = Modifier,
    navController: NavController,
    scannerState: QRScannerState,
    onScannerEvent: (QRScannerEvents) -> Unit
) {

    var amount by remember { mutableStateOf("") }
    var selectedUpiId by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Merchant") }
    val upiIds = listOf("ronit@ybl", "chinda@ybl", "test@ybl")
    val context = LocalContext.current
    var gstin by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var canContinuePayment by remember {
        mutableStateOf(false)
    }
    val isMerchant=false

    if(isMerchant){
        LaunchedEffect(key1 = gstin, key2 = amount) {
            canContinuePayment = gstin.isNotBlank() && amount.isNotBlank() && amount.toFloat() > 0
        }
    }

    if(!isMerchant){
        LaunchedEffect(key1 = amount) {
            canContinuePayment = amount.isNotBlank() && amount.toFloat() > 0
        }
    }


    BackHandler {
        onScannerEvent(QRScannerEvents.ClearState)
        navController.navigate(NavRoutes.Home.route) {
            popUpTo(NavRoutes.AddTransactionDetails.route) {
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
                uName = "Ronit Chinda",
                uUpiId = "chinda@ybl",
            )

            if(isMerchant) {
                GSTVerifiedButton()
                MerchantPaymentDetails(
                    onGSTChange = {
                        gstin = it
                    },
                    onDropDownSelection = {
                        selectedOption = it
                    }
                )
            }
            if(!isMerchant) Spacer(modifier =modifier.height(50.dp))
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
                upiIds = upiIds,
                selectedUpiId = selectedUpiId,
                onUpiIdSelected = { id ->
                    selectedUpiId = id
                },
                onPayClick = {
                    scope.launch {
                        sheetState.hide()
                        showBottomSheet = false
                    }
                    navController.navigate(NavRoutes.TransactionReceipt.route)
                }
            )
        }
    }
}

@Composable
fun MerchantPaymentDetails(
    onDropDownSelection: (String) -> Unit,
    onGSTChange: (String) -> Unit
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
    OutlinedTextField(
        value = gstin,
        onValueChange = { input ->
            gstin = input
            onGSTChange("")
            if (input.length == 15) {
                if (input.matches(Regex("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$"))) {
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

@Composable
fun GSTVerifiedButton() {
    AssistChip(
        onClick = { },
        label = { Text("GST Verified") },
        leadingIcon = {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = "Localized description",
                Modifier.size(AssistChipDefaults.IconSize)
            )
        },
        modifier = Modifier.padding(vertical = 15.dp)
    )
}

@Composable
fun BottomSheetContent(
    upiIds: List<String>,
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
        upiIds.forEach { upiId ->
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