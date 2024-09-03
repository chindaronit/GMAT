package com.gmat.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUs(
    modifier: Modifier=Modifier
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
                        "About Us",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            Card(
                modifier = modifier.padding(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(modifier = modifier.padding(16.dp).alpha(0.8f),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.W500,
                    text =
                        "Welcome to our GMAT, where innovation meets responsibility. Our application is designed to revolutionize the way you conduct transactions, bringing the power of UPI technology to your fingertips with an added layer of purpose.\n" +
                        "\n" +
                        "In association with the Government of India, our platform ensures that every merchant transaction you make is seamlessly recorded for GST compliance. By doing so, we are helping to create a transparent and accountable system where no one can evade their tax obligations. This initiative is part of our commitment to support the nation's economic growth by ensuring that every transaction contributes to the greater good.\n" +
                        "\n" +
                        "But our mission doesn't stop there. We believe in rewarding those who contribute to this cause. Through the government's \"Mera Bill Mera Adhikaar\" scheme, customers who report their transactions are eligible for special rewards. It's our way of saying thank you for helping build a stronger and more prosperous India.\n" +
                        "\n" +
                        "Join us in making a difference. Together, we can create a future where every transaction counts, for both you and the nation.")
            }
        }

    }
}