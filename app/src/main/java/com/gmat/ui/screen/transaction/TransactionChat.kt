package com.gmat.ui.screen.transaction

import androidx.navigation.NavController
import com.gmat.ui.components.CenterBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmat.R
import com.gmat.data.model.TransactionModel
import com.gmat.env.formatDateWithDay
import com.gmat.navigation.NavRoutes
import com.gmat.ui.state.TransactionState
import com.gmat.ui.state.UserState
import com.gmat.ui.theme.DarkGreen
import com.gmat.ui.theme.DarkRed

// TransactionChat screen implementation
@Composable
fun TransactionChat(
    modifier: Modifier = Modifier,
    navController: NavController,
    userState: UserState,
    transactionState: TransactionState,
) {
    Scaffold(
        topBar = {
            CenterBar(
                onClick = { navController.navigateUp() },
                title = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Ronit Chinda",
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "UPI ID: chinda@ybl",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            userState.user?.let { user ->
                if (!user.isMerchant) {
                    ExtendedFloatingActionButton(
                        onClick = { navController.navigate(NavRoutes.AddTransactionDetails.route) }
                    ) {
                        Text("Pay")
                    }
                }
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 80.dp)
        ) {
            itemsIndexed(transactionState.trasactionChat, key = { index, _ -> index }) { _, transaction ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (userState.user?.isMerchant == true) {
                        // Merchant: Show card on the left side
                        TransactionCard(
                            navController = navController,
                            transaction = transaction,
                            modifier = Modifier.weight(1f) // Card on the left side
                        )
                        Spacer(modifier = Modifier.weight(1f)) // Push content to the right
                    } else {
                        Spacer(modifier = Modifier.weight(1f)) // Pushes the card to the right
                        TransactionCard(
                            navController = navController,
                            transaction = transaction,
                            modifier = Modifier.weight(1f) // Card on the right side
                        )
                    }
                }
            }
        }

    }
}

// TransactionCard composable to display transaction details
@Composable
fun TransactionCard(transaction: TransactionModel, modifier: Modifier = Modifier, navController: NavController) {
    Card(
        onClick = { navController.navigate(NavRoutes.TransactionReceipt.route) },
        modifier = modifier
            .padding(8.dp), // Card-specific padding
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 14.dp) // Padding inside the card
        ) {
            Column(
                modifier = Modifier.fillMaxWidth() // Ensures column takes full width of the card
            ) {
                Text(
                    text = getTransactionStatusText(transaction.status),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 0.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = transaction.amount,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = formatDateWithDay(transaction.timestamp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Status image at the top right corner
            val (statusImage, statusColor) = when (transaction.status) {
                1 -> Pair(R.drawable.success, DarkGreen) // Success: Green
                else -> Pair(R.drawable.failed, DarkRed)
            }

            Icon(
                painter = painterResource(id = statusImage),
                contentDescription = getTransactionStatusText(transaction.status),
                modifier = Modifier
                    .align(Alignment.TopEnd) // Aligns the icon to the top right of the card
                    .size(30.dp),
                tint = statusColor
            )
        }
    }
}

fun getTransactionStatusText(status: Int): String {
    return when (status) {
        1 -> "Completed"
        2 -> "Pending"
        else -> "Failed"
    }
}



//package com.gmat.ui.screen.transaction
//
//import androidx.navigation.NavController
//import com.gmat.ui.components.CenterBar
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.gmat.R
//import com.gmat.env.formatDateWithDay
//import com.gmat.navigation.NavRoutes
//import com.gmat.ui.state.TransactionState
//import com.gmat.ui.state.UserState
//import com.gmat.ui.theme.DarkGreen
//import com.gmat.ui.theme.DarkRed
//import java.util.*
//
//@Composable
//fun TransactionChat(
//    modifier: Modifier = Modifier,
//    navController: NavController,
//    userState: UserState,
//    transactionState: TransactionState,
//) {
////    val transactions = listOf(
////        Transaction("₹500.00", "Completed", Date(2024, 8, 10, 14, 30)),
////        Transaction("₹1500.00", "Pending", Date(2024, 8, 11, 9, 15)),
////        Transaction("₹750.00", "Failed", Date(2024, 8, 12, 18, 45)),
////        Transaction("₹100000.00", "Completed", Date(2024, 8, 13, 12, 0)),
////        Transaction("₹200.00", "Completed", Date(2024, 8, 14, 16, 30)),
////        Transaction("₹1200.00", "Completed", Date(2024, 8, 13, 12, 0)),
////        Transaction("₹200.00", "Completed", Date(2024, 8, 14, 16, 30)),
////        Transaction("₹500.00", "Completed", Date(2024, 8, 10, 14, 30)),
////        Transaction("₹1500.00", "Pending", Date(2024, 8, 11, 9, 15)),
////        Transaction("₹750.00", "Failed", Date(2024, 8, 12, 18, 45)),
////        Transaction("₹1200.00", "Completed", Date(2024, 8, 13, 12, 0)),
////        Transaction("₹200.00", "Completed", Date(2024, 8, 14, 16, 30)),
////        Transaction("₹1200.00", "Completed", Date(2024, 8, 13, 12, 0)),
////        Transaction("₹200.00", "Completed", Date(2024, 8, 14, 16, 30)),
////    )
//
//    Scaffold(
//        topBar = {
//            CenterBar(
//                onClick = {navController.navigateUp()},
//                title = {
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                    ) {
//                        Text(
//                            text = "Ronit Chinda",
//                            style = MaterialTheme.typography.titleLarge,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                        Text(
//                            text = "UPI ID: chinda@ybl",
//                            style = MaterialTheme.typography.bodyMedium,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                    }
//                }
//            )
//        },
//
//        floatingActionButton = {
//            userState.user?.let { user ->
//                if (!user.isMerchant) {
//                    ExtendedFloatingActionButton(
//                        onClick = { navController.navigate(NavRoutes.AddTransactionDetails.route) }
//                    ) {
//                        Text("Pay")
//                    }
//                }
//            }
//        }
//    ) { innerPadding ->
//
//        LazyColumn(
//            modifier = modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(bottom = 80.dp)
//        ) {
//            itemsIndexed(transactionState.trasactionChat, key = { index, _ -> index }) { _, transaction ->
//                Row(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    // Check if the user is a merchant
//                    if (userState.user?.isMerchant == true) {
//                        // Merchant: Show card on the left side
//                        TransactionCard(
//                            navController = navController,
//                            transaction = transaction,
//                            modifier = Modifier.weight(1f) // Card on the left side
//                        )
//                        Spacer(modifier = Modifier.weight(1f)) // Push content to the right
//                    } else {
//                        // Non-merchant: Show card on the right side
//                        Spacer(modifier = Modifier.weight(1f)) // Pushes the card to the right
//                        TransactionCard(
//                            navController = navController,
//                            transaction = transaction,
//                            modifier = Modifier.weight(1f) // Card on the right side
//                        )
//                    }
//                }
//            }
//        }
//
//    }
//}
//
//@Composable
//fun TransactionCard(transaction: Transaction, modifier: Modifier = Modifier, navController: NavController) {
//    Card(
//        onClick = {navController.navigate(NavRoutes.TransactionReceipt.route)},
//        modifier = modifier
//            .padding(8.dp), // Card-specific padding
//        elevation = CardDefaults.cardElevation(4.dp),
//        colors = CardDefaults.cardColors(
//            contentColor = MaterialTheme.colorScheme.onPrimary,
//            containerColor = MaterialTheme.colorScheme.primary
//        )
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 24.dp, horizontal = 14.dp) // Padding inside the card
//        ) {
//            Column(
//                modifier = Modifier.fillMaxWidth() // Ensures column takes full width of the card
//            ) {
//                Text(
//                    text = transaction.status,
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontSize = 12.sp
//                    ),
//                    modifier = Modifier
//                        .align(Alignment.Start)
//                        .padding(top = 0.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                Text(
//                    text = transaction.amount,
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//                Spacer(modifier = Modifier.height(5.dp))
//                Text(
//                    text = formatDateWithDay(Date()),
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontSize = 10.sp
//                    ),
//                    modifier = Modifier.padding(top = 4.dp)
//                )
//            }
//
//            // Status image at the top right corner
//            val (statusImage, statusColor) = when (transaction.status) {
//                "Completed" -> Pair(R.drawable.success, DarkGreen) // Success: Green
//                else -> Pair(R.drawable.failed, DarkRed)
//            }
//
//            Icon(
//                painter = painterResource(id = statusImage),
//                contentDescription = transaction.status,
//                modifier = Modifier
//                    .align(Alignment.TopEnd) // Aligns the icon to the top right of the card
//                    .size(30.dp),
//                tint = statusColor
//            )
//        }
//    }
//}
//
//
//data class Transaction(
//    val amount: String,
//    val status: String,
//    val date: Date
//)