package com.gmat.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.ui.components.CenterBar

@Composable
fun EditProfileDetails(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    Scaffold(
        topBar = {
            CenterBar(
                navController = navController,
                title = {
                    Text(
                        text = stringResource(id = R.string.edit_profile),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                    }
                })
        },
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = null,
                modifier = modifier.size(150.dp)
            )
            Button(onClick = { }) {
                Text(text = stringResource(id = R.string.upload_photo), fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            }
            Spacer(modifier = modifier.height(20.dp))
            OutlinedTextField(
                value = "Ronit Chinda",
                onValueChange = {},
                modifier = modifier.fillMaxWidth(),
                label = { Text("Name") })
        }
    }
}