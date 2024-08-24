package com.gmat.ui.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile() {
    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        "Profile",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier.padding(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,

                    ) {
                    Icon(
                        painter = painterResource(R.drawable.user_icon),
                        contentDescription = "",
                        modifier = Modifier.size(100.dp)
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 30.dp)
                    ) {
                        Text(
                            text = "Ronit Chinda",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp
                        )
                        Text(text = "UPI ID: chinda@sbi")
                        Text(text = "7988224882", fontWeight = FontWeight.Bold,)
                    }
                }
            }

            Column(
                modifier = Modifier.padding(vertical = 10.dp),

            ) {
                Card(
                    modifier = Modifier.padding(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.qr_code_icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text("Upgraded QR", modifier = Modifier.padding(horizontal = 15.dp), fontSize = 20.sp)
                    }
                }
                Card(
                    modifier = Modifier.padding(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.question_icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text("Help", modifier = Modifier.padding(horizontal = 15.dp), fontSize = 20.sp)
                    }
                }
                Card(
                    modifier = Modifier.padding(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.information_icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text("About Us", modifier = Modifier.padding(horizontal = 15.dp), fontSize = 20.sp)
                    }
                }
                Card(
                    modifier = Modifier.padding(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.sign_out_icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp),
                            tint = MaterialTheme.colorScheme.primary

                        )

                        Text("Sign Out", modifier = Modifier.padding(horizontal = 15.dp), fontSize = 20.sp)
                    }
                }
            }

        }
    }
}
