package com.gmat.ui.screen.transaction

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.env.formatDate
import com.gmat.ui.theme.DarkGreen
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistory(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(
                        "History",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            LazyRow(
                modifier = modifier.padding(12.dp)
            ) {
                item {
                    AssistChip(
                        onClick = { },
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
                        onClick = { },
                        label = { Text("Transaction Type") }, leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        })
                }
            }
            Card(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
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
                items(4) {
                    Card(
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable {

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
                                contentDescription = "",
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

                            Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = null, tint = DarkGreen)
                            Text(
                                text = "100000",
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .widthIn(max = 100.dp),
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = DarkGreen
                            )
                        }
                    }

                }
            }

        }
    }
}