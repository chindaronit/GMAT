package com.gmat.ui.screen.merchant

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gmat.R
import com.gmat.ui.components.CenterBar

@Composable
fun UpgradedQR(
    modifier: Modifier=Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterBar(
                navController = navController,
                title = {
                    Text(
                        text = stringResource(id = R.string.upgraded_qr),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        }
    ) { contentPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(top = 20.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // QR Icon
            AsyncImage(
                model = R.drawable.qr,
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth(),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )

            Spacer(modifier = modifier.height(20.dp))
            // UPI ID Text
            Text(
                text = "UPI ID: chinda@ybl",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
            )

            Spacer(modifier = modifier.weight(1f))

            // Download Button at the bottom
            Button(
                onClick = {

                },
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Text("Download")
            }
        }
    }
}
