package com.gmat.ui.screen.login


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmat.ui.components.login.Bottom
import com.gmat.ui.components.login.Top

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register() {

    var name by remember {
        mutableStateOf("")
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }

    val values = listOf("Personal", "Merchant")
    var currentVal = "Personal"

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text("GMAT")
                }
            )
        },
    )
    { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Top()
            }
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Complete your profile",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        if (it.all { char -> char.isLetter() || char == ' ' }) {
                            name = it
                        }
                    },
                    placeholder = { Text(text = "Enter your Name", fontFamily = FontFamily.Monospace) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }
                ) {
                    OutlinedTextField(
                        value = currentVal,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },

                        modifier = Modifier
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                    ) {
                        values.forEach { label ->
                            DropdownMenuItem(
                                text = {
                                    Text(label)
                                },
                                onClick = {
                                    currentVal = label
                                    isExpanded = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { }) {
                    Text("Get Started", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                Bottom()
            }
        }
    }
}
