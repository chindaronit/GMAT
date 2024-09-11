package com.gmat.ui.components.login

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.gmat.R

@Composable
fun Top(
    modifier: Modifier=Modifier
) {
    AsyncImage(
        model = R.drawable.logo,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    )
}