package com.gmat.ui.screen.transaction

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.transaction.ProfileTransactionCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDetails(
    modifier: Modifier=Modifier,
    navController: NavController
) {

    var amount by remember { mutableStateOf("") }
    var selectedUpiId by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Merchant") }
    val upiIds = listOf("ronit@ybl", "chinda@ybl", "test@ybl")
    val options = listOf("Merchant", "Personal")
    var gstin by remember { mutableStateOf("") }

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text("", maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation click */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        floatingActionButton = {
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileTransactionCard(
                    uName = "Ronit Chinda",
                    uUpiId = "chinda@ybl",
                )

                Spacer(modifier = Modifier.height(10.dp))
                GSTVerifiedButton()
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(horizontal = 40.dp)
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
                        modifier = Modifier.width(225.dp),
                        onDismissRequest = { expanded = false },
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedOption = option
                                    expanded = false

                                },
//                                modifier = Modifier.fillMaxWidth(),
                                text = { Text(text = option)
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = gstin,
                    onValueChange = { input ->
                        gstin = input
                        if (input.length == 15) {
                            if (input.matches(Regex("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$"))) {
                                gstin = input
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
                        .width(300.dp)  // Fixed width
                        .padding(horizontal = 40.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    placeholder = { Text("Enter Amount") },
                    value = amount,
                    onValueChange = { input ->
                        if (input.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
                            if (input.isNotEmpty() && input.toDouble() > 100000) {
                                Toast.makeText(context, "Money should not exceed 100,000", Toast.LENGTH_SHORT).show()
                            } else {
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
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .width(300.dp)
                        .padding(horizontal = 40.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))


            }
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
fun GSTVerifiedButton() {
    AssistChip(
        onClick = { /* Handle click */ },
        label = { Text("GST Verified") },
        leadingIcon = {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = "Localized description",
                Modifier.size(AssistChipDefaults.IconSize)
            )
        }
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