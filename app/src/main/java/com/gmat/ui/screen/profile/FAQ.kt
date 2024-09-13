package com.gmat.ui.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.ui.components.CenterBar

@Composable
fun FAQ(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterBar(
                onClick = {navController.navigateUp()},
                title = {
                    Text(
                        text = stringResource(id = R.string.faq),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        },
        content = { innerPadding ->
            FAQContent(modifier = modifier.padding(innerPadding))
        }
    )
}

@Composable
fun FAQContent(modifier: Modifier = Modifier) {
    val faqList = listOf(
        FAQItem(
            "How do I reset my password?",
            "To reset your password, go to the settings page and click on 'Reset Password'. You will receive an email with instructions."
        ),
        FAQItem(
            "How can I contact support?",
            "You can contact support by emailing support@example.com or using the chat feature on our website."
        ),
        FAQItem(
            "Where can I find my purchase history?",
            "Your purchase history can be found under the 'Orders' section in your profile."
        ),
        // Add more FAQ items here
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(faqList) { faqItem ->
            FAQItemCard(faqItem)
        }
    }
}

@Composable
fun FAQItemCard(faqItem: FAQItem, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = faqItem.question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = modifier.rotate(rotation)
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {
                    Spacer(modifier = modifier.height(8.dp))
                    Text(
                        text = faqItem.answer,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

data class FAQItem(val question: String, val answer: String)
