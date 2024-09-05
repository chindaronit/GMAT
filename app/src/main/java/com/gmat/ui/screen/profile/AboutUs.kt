package com.gmat.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.ui.components.CenterBar

@Composable
fun AboutUs(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterBar(
                navController = navController,
                title = {
                    Text(
                        text = stringResource(id = R.string.about_us),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
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
                Text(
                    modifier = modifier
                        .padding(16.dp)
                        .alpha(0.8f),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.W500,
                    text = stringResource(id = R.string.about_us_data)
                )
            }
        }

    }
}