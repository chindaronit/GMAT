package com.gmat.ui.screen.merchant


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.ui.components.CenterBar

@Composable
fun ScanQR(
    navController: NavController
) {
    val context = LocalContext.current
    var gstin by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterBar(
                navController = navController,
                title = {
                    Text(
                        text = "Scan QR",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        }
    ) { contentPadding ->

        // Main Content Layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // QR Icon
            Icon(
                painter = painterResource(id = R.drawable.scanner), // Replace with your QR icon resource
                contentDescription = "Scanner",
                modifier = Modifier
                    .size(250.dp)  // Increased size
                    .padding(top = 32.dp)
            )


            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Cancel")
            }
        }
    }
}