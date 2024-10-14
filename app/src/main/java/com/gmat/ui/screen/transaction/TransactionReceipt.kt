package com.gmat.ui.screen.transaction

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.env.formatDate
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.CenterBar
import com.gmat.ui.components.ReceiptPreloader
import com.gmat.ui.components.transaction.ProfileTransactionCard
import com.gmat.ui.events.TransactionEvents
import com.gmat.ui.state.TransactionState
import com.gmat.ui.state.UserState
import com.gmat.ui.theme.DarkGreen

@Composable
fun TransactionReceipt(
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionState: TransactionState,
    userState: UserState,
    onTransactionEvents: (TransactionEvents) -> Unit,
    txnId: String,
    userId: String
) {

    LaunchedEffect(key1 = Unit) {
        println("$userId, $txnId")
        onTransactionEvents(TransactionEvents.GetTransactionById(userId = userId, txnId = txnId))
    }

    BackHandler {
        navController.navigate(NavRoutes.Home.route) {
            popUpTo(NavRoutes.TransactionReceipt.route) {
                inclusive = true  // This clears the entire back stack
            }
            launchSingleTop = true  // Avoid creating multiple instances of the Home screen
        }
    }


    Scaffold(
        topBar = {
            CenterBar(
                onClick = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.TransactionReceipt.route) {
                            inclusive = true  // This clears the entire back stack
                        }
                        launchSingleTop =
                            true  // Avoid creating multiple instances of the Home screen
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.receipt),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        }) { innerPadding ->
        if (transactionState.isLoading) {
            Column(
                modifier = modifier
                    .padding(innerPadding)
            ) {
                ReceiptPreloader()
            }
        }

        if (transactionState.transaction != null && !transactionState.isLoading) {
            val transaction = transactionState.transaction
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileTransactionCard(
                    uName = transaction.name,
                    uUpiId = transaction.payeeId,
                    userState.user!!.isMerchant
                )
                Spacer(modifier = Modifier.height(50.dp))
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.CurrencyRupee,
                        contentDescription = "Currency Rupee",
                        modifier = modifier.size(36.dp)
                    )
                    Text(
                        text = transaction.amount,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.success),
                        contentDescription = null,
                        tint = DarkGreen,
                        modifier = modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))

                ReceiptCard(
                    modifier = modifier,
                    date = formatDate(transaction.timestamp),
                    type = if (transaction.type == 0
                    ) "Merchant" else "Personal",
                    gstin = transaction.gstin,
                    payee = transaction.payeeId,
                    payer = transaction.payerId,
                    txnId = transaction.txnId
                )
            }
        }
    }

}

@Composable
fun ReceiptCard(
    modifier: Modifier,
    date: String,
    type: String,
    gstin: String,
    payer: String,
    payee: String,
    txnId: String
) {
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.5.dp, // Border width
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), // Border color
                shape = RoundedCornerShape(8.dp) // Border shape matching the card shape
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 15.dp, vertical = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ) {
            Column(
                modifier = modifier
                    .padding(horizontal = 4.dp)
                    .alpha(0.9f)
            ) {
                Text(
                    text = "Date: $date",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .alpha(0.9f)
                )
                Text(
                    text = "Type: $type",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .alpha(0.9f)
                )
                Text(
                    text = "GSTIN: $gstin",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .alpha(0.9f)
                )
                Text(
                    text = "PayerID: $payer",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .alpha(0.9f)
                )
                Text(
                    text = "PayeeID: $payee",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .alpha(0.9f)
                )
                Text(
                    text = "TransactionID: $txnId",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .alpha(0.9f)
                )
            }
        }
    }
}