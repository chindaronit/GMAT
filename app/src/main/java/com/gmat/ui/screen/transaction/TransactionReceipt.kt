package com.gmat.ui.screen.transaction

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
import com.gmat.ui.components.transaction.ProfileTransactionCard
import com.gmat.ui.theme.DarkGreen
import java.util.Date

@Composable
fun TransactionReceipt(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterBar(
                onClick = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.TransactionReceipt.route) {
                            inclusive = true
                        } // Clears the back stack
                        launchSingleTop = true  // Avoids multiple instances of the screen
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.receipt),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        },
        content = { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileTransactionCard(
                    uName = "Ronit Chinda",
                    uUpiId = "chinda@ybl",
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
                        text = "6000",
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
                    date = formatDate(Date()),
                    type = "Merchant",
                    gstin = "07AAECR2971C1Z",
                    payee = "chinda@sbi",
                    payer = "vishal@ybl",
                    txnId = "424855509757"
                )
            }
        }
    )
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