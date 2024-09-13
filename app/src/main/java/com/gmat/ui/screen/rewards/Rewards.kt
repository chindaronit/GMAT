package com.gmat.ui.screen.rewards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.ui.components.CenterBar
import com.gmat.ui.components.RenderPainterIcon

@Composable
fun Rewards(
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterBar(
                onClick = {navController.navigateUp()},
                title = {
                    Text(
                        text = stringResource(id = R.string.rewards),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RenderPainterIcon(id = R.drawable.reward_icon, modifier = Modifier.size(100.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 30.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.your_rank)+":",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = stringResource(id = R.string.your_points)+":",
                            fontWeight = FontWeight.ExtraLight,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.leaderboard),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 15.dp, top = 20.dp, bottom = 10.dp)
                    )
                }

                LeaderboardEntry(
                    name = "Ronit Chinda",
                    points = "1000",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )

                LeaderboardEntry(
                    name = "Vishal Kumar Prajapati",
                    points = "1000",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun LeaderboardEntry(name: String, points: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
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

            RenderPainterIcon(id = R.drawable.reward_icon, modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .border(
                    BorderStroke(3.dp, MaterialTheme.colorScheme.onSurface),
                    CircleShape
                )
                .padding(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 15.dp),
            ) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.widthIn(max = 150.dp)
                )
            }
            Text(
                text = points,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

