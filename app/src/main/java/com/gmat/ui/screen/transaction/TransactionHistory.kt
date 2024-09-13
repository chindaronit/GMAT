package com.gmat.ui.screen.transaction

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.env.formatDate
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.CenterBar
import com.gmat.ui.theme.DarkGreen
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistory(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val dateValues = listOf("Last 10 Days", "Last 30 Days", "This Month")
    val typeValues = listOf("Merchant", "Personal")
    var selectedTypeValue by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showDateSheet by remember { mutableStateOf(false) }
    var showTypeSheet by remember { mutableStateOf(false) }
    var selectedDateValue by remember { mutableStateOf("") }

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
            Filters(onDateClick = { showDateSheet = true }, onTypeClick = { showTypeSheet = true })
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
                    text = "August 2024",
                    modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
            LazyColumn {
                items(5) {
                    Card(
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable {
                                navController.navigate(NavRoutes.TransactionReceipt.route)
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
                                        BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.onSurface
                                        ),
                                        CircleShape
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 15.dp),
                            ) {
                                Text(
                                    text = "Ronit Chinda",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.widthIn(max = 150.dp)
                                )
                                Text(
                                    text = formatDate(Date()),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light,
                                )
                            }

                            Icon(
                                imageVector = Icons.Filled.CurrencyRupee,
                                contentDescription = null,
                                tint = Color.Red
                            )

                            Text(
                                text = "-100000",
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .widthIn(max = 100.dp),
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Red
                            )
                        }
                    }

                }
            }

            if (showDateSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showDateSheet = false
                    },
                    sheetState = sheetState,
                    properties = ModalBottomSheetProperties(
                        isFocusable = true,
                        securePolicy = SecureFlagPolicy.SecureOn,
                        shouldDismissOnBackPress = false
                    )
                ) {
                    SheetContent(values = dateValues, selectedValueId = selectedDateValue) {
                        selectedDateValue = it
                        scope.launch {
                            sheetState.hide()
                            showDateSheet = false
                        }
                    }
                }
            }

            if (showTypeSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showTypeSheet = false
                    },
                    sheetState = sheetState,
                    properties = ModalBottomSheetProperties(
                        isFocusable = true,
                        securePolicy = SecureFlagPolicy.SecureOn,
                        shouldDismissOnBackPress = false
                    )
                ) {
                    SheetContent(values = typeValues, selectedValueId = selectedTypeValue) {
                        selectedTypeValue = it
                        scope.launch {
                            sheetState.hide()
                            showTypeSheet = false
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SheetContent(
    values: List<String>,
    selectedValueId: String,
    onSelect: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        values.forEach { id ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(id) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = id,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                )
                RadioButton(
                    selected = selectedValueId == id,
                    onClick = {
                        onSelect(id)
                    }
                )
            }
        }
    }
}



@Composable
fun Filters(modifier: Modifier = Modifier, onDateClick: () -> Unit, onTypeClick: () -> Unit) {
    LazyRow(
        modifier = modifier.padding(12.dp)
    ) {
        item {
            AssistChip(
                onClick = { onDateClick() },
                label = { Text("Date") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                })
        }

        item {
            Spacer(modifier = Modifier.width(8.dp))
        }

        item {
            AssistChip(
                onClick = { onTypeClick() },
                label = { Text("Type") }, leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                })
        }
    }
}