package com.gmat.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun HomeScreenPreloader(modifier: Modifier = Modifier) {
    Scaffold { innerpadding ->
        Column(modifier = modifier.padding(innerpadding)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp, horizontal = 10.dp)
                    .height(250.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .placeholder(
                        visible = true, // Set to `true` while loading
                        color = Color.Gray, // Placeholder background color
                        shape = RoundedCornerShape(8.dp), // Placeholder shape
                        highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                    )
            ) {}

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 25.dp, horizontal = 20.dp)
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .placeholder(
                                visible = true, // Set to `true` while loading
                                color = Color.Gray, // Placeholder background color
                                shape = RoundedCornerShape(8.dp), // Placeholder shape
                                highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                            )
                    ) {}
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(4) {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .placeholder(
                                visible = true, // Set to `true` while loading
                                color = Color.Gray, // Placeholder background color
                                shape = RoundedCornerShape(8.dp), // Placeholder shape
                                highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                            )
                    ) {}
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(4) {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .placeholder(
                                visible = true, // Set to `true` while loading
                                color = Color.Gray, // Placeholder background color
                                shape = RoundedCornerShape(8.dp), // Placeholder shape
                                highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                            )
                    ) {}
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(4) {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .placeholder(
                                visible = true, // Set to `true` while loading
                                color = Color.Gray, // Placeholder background color
                                shape = RoundedCornerShape(8.dp), // Placeholder shape
                                highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                            )
                    ) {}
                }
            }
        }
    }
}

@Composable
fun RecentTransactionPreloader(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(4) {
            Card(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .placeholder(
                        visible = true, // Set to `true` while loading
                        color = Color.Gray, // Placeholder background color
                        shape = RoundedCornerShape(8.dp), // Placeholder shape
                        highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                    )
            ) {}
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(4) {
            Card(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .placeholder(
                        visible = true, // Set to `true` while loading
                        color = Color.Gray, // Placeholder background color
                        shape = RoundedCornerShape(8.dp), // Placeholder shape
                        highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                    )
            ) {}
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(4) {
            Card(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .placeholder(
                        visible = true, // Set to `true` while loading
                        color = Color.Gray, // Placeholder background color
                        shape = RoundedCornerShape(8.dp), // Placeholder shape
                        highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                    )
            ) {}
        }
    }
}

@Composable
fun TransactionPreloader() {
    Column{
        repeat(6) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 15.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .placeholder(
                        visible = true, // Set to `true` while loading
                        color = Color.Gray, // Placeholder background color
                        shape = RoundedCornerShape(8.dp), // Placeholder shape
                        highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                    )
            ) {}
        }
    }
}

@Composable
fun ReceiptPreloader(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 50.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp, horizontal = 10.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .placeholder(
                    visible = true, // Set to `true` while loading
                    color = Color.Gray, // Placeholder background color
                    shape = RoundedCornerShape(8.dp), // Placeholder shape
                    highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                )
        ) {}

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 50.dp, horizontal = 10.dp)
                .height(400.dp)
                .clip(RoundedCornerShape(8.dp))
                .placeholder(
                    visible = true, // Set to `true` while loading
                    color = Color.Gray, // Placeholder background color
                    shape = RoundedCornerShape(8.dp), // Placeholder shape
                    highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                )
        ) {}
    }
}

@Composable
fun ProfilePreloader(modifier: Modifier = Modifier) {
    Column{
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 15.dp)
                .height(250.dp)
                .clip(RoundedCornerShape(10.dp))
                .placeholder(
                    visible = true, // Set to `true` while loading
                    color = Color.Gray, // Placeholder background color
                    shape = RoundedCornerShape(8.dp), // Placeholder shape
                    highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                )
        ) {}

        repeat(4) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 15.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .placeholder(
                        visible = true, // Set to `true` while loading
                        color = Color.Gray, // Placeholder background color
                        shape = RoundedCornerShape(8.dp), // Placeholder shape
                        highlight = PlaceholderHighlight.shimmer(Color.LightGray) // Shimmer effect
                    )
            ) {}
        }
    }
}
