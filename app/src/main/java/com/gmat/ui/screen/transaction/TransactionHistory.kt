package com.gmat.ui.screen.transaction

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.data.model.TransactionModel
import com.gmat.data.model.UserModel
import com.gmat.env.formatDate
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.CenterBar
import com.gmat.ui.components.MonthYearPicker
import com.gmat.ui.components.TransactionPreloader
import com.gmat.ui.events.TransactionEvents
import com.gmat.ui.state.TransactionState
import com.gmat.ui.state.UserState
import com.gmat.ui.theme.DarkGreen
import com.gmat.ui.theme.DarkRed
import java.util.Calendar

@Composable
fun TransactionHistory(
    modifier: Modifier = Modifier,
    navController: NavController,
    isLoading: Boolean,
    user: UserModel,
    transactionHistory: List<TransactionModel>? = null,
    onTransactionEvents: (TransactionEvents) -> Unit,
) {
    val months = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    val calendar = Calendar.getInstance()
    val currMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based, add 1
    val currYear = calendar.get(Calendar.YEAR)
    var visible by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableIntStateOf(currMonth) }
    var selectedYear by remember { mutableIntStateOf(currYear) }

    LaunchedEffect(
        key1 = transactionHistory,
        key2 = selectedMonth,
        key3 = selectedYear
    ) {
        if (transactionHistory == null) {
            if (user.isMerchant) {
                onTransactionEvents(
                    TransactionEvents.GetAllTransactionsForMonth(
                        userId = null,
                        month = selectedMonth,
                        year = selectedYear,
                        vpa = user.vpa
                    )
                )
            } else {
                onTransactionEvents(
                    TransactionEvents.GetAllTransactionsForMonth(
                        userId = user.userId,
                        month = selectedMonth,
                        year = selectedYear,
                        vpa = null
                    )
                )
            }
        }
    }

    Scaffold(
        topBar = {
            CenterBar(
                onClick = { navController.navigateUp() },
                title = {
                    Text(
                        text = stringResource(id = R.string.history),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        }
    ) { innerPadding ->

        Column(
            modifier = modifier.padding(innerPadding)
        ) {

            DateFilter {
                visible = true
            }

            MonthYearPicker(
                visible = visible,
                currentMonth = selectedMonth - 1,
                currentYear = selectedYear,
                onConfirmation = { month, year ->
                    selectedMonth = month
                    selectedYear = year
                    visible = false
                    onTransactionEvents(TransactionEvents.ClearTransactionHistory)
                },
                onDismissRequest = {
                    visible = false
                }
            )

            Card(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = months[selectedMonth - 1] + ", $selectedYear",
                    modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
            if (isLoading) {
                TransactionPreloader()
            }

            if (transactionHistory != null && !isLoading) {
                if (transactionHistory.isEmpty()) {
                    Text(
                        text = "No transactions found!",
                        modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
                LazyColumn {
                    // Use 'items' to iterate over the list of transactions
                    items(transactionHistory) { transaction ->
                        Card(
                            modifier = Modifier
                                .padding(5.dp)
                                .clickable {
                                    if (user.isMerchant) {
                                        navController.navigate(
                                            NavRoutes.TransactionReceipt.withArgs(
                                                transaction.txnId,
                                                transaction.payerUserId
                                            )
                                        )
                                    } else {
                                        navController.navigate(
                                            NavRoutes.TransactionReceipt.withArgs(
                                                transaction.txnId,
                                                user.userId
                                            )
                                        )
                                    }
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.user_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(45.dp)
                                        .clip(CircleShape)
                                        .border(
                                            border = BorderStroke(
                                                1.dp,
                                                MaterialTheme.colorScheme.onSurface
                                            ), shape = CircleShape
                                        )
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 15.dp),
                                ) {
                                    // Display transaction name
                                    Text(
                                        text = transaction.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        modifier = Modifier.widthIn(max = 150.dp)
                                    )
                                    // Display additional details (e.g., transaction date)
                                    Text(
                                        text = formatDate(transaction.timestamp),  // Adjust the content based on your data model
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Light,
                                    )
                                }

                                // Display transaction amount
                                Text(
                                    text = if (user.isMerchant) "+ ₹${transaction.amount}" else "- ₹${transaction.amount}",
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .widthIn(max = 100.dp),
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = if (user.isMerchant) DarkGreen else DarkRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DateFilter(onClick: () -> Unit) {
    AssistChip(
        onClick = { onClick() },
        label = { Text("Filter") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null
            )
        },
        modifier = Modifier.padding(start = 10.dp)
    )
}