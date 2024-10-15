package com.gmat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomToast(
    modifier: Modifier,
    message: String,
    bottomPadding: Dp=0.dp,
    isVisible: Boolean,
) {
    if (isVisible) {
        Row(
            modifier = modifier
                .padding(bottom = bottomPadding)
                .clip(RoundedCornerShape(10.dp)) // Rounded corners
                .background(MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically, // Align elements vertically to the center
            horizontalArrangement = Arrangement.Start // Start arrangement ensures elements are close together
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(28.dp) // Size of the indicator
                    .padding(top=3.dp, start = 4.dp, end = 8.dp), // Space between indicator and text
                color = MaterialTheme.colorScheme.onSurface // Progress indicator color
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface, // Text color
                modifier = Modifier.align(Alignment.CenterVertically) // Ensure text stays vertically aligned
            )
        }
    }
}

