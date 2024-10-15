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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PermIdentity
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.gmat.data.model.UserModel
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.login.Bottom
import com.gmat.ui.components.login.Top
import com.gmat.ui.events.UserEvents
import com.gmat.ui.state.UserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
    modifier: Modifier = Modifier,
    navController: NavController,
    userState: UserState,
    onUserEvents: (UserEvents)->Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var vpa by remember {
        mutableStateOf("")
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }

    val values = listOf("Personal", "Merchant")
    var currentVal = "Personal"

    LaunchedEffect(key1 = userState.user) {
        if(userState.user!=null){
            if(userState.user.phNo.isNotBlank()){
                onUserEvents(UserEvents.UpdateRoom(user = userState.user, verificationId = userState.verificationId))
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(0) { inclusive = true } // This removes everything from the backstack
                    launchSingleTop = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = { Text("GMAT", style = MaterialTheme.typography.headlineLarge) }
            )
        },
    )
    { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .align(Alignment.TopCenter)
            ) {
                Top()
            }
            Column(
                modifier = modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Complete your profile",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = modifier.height(20.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        if (it.all { char -> char.isLetter() || char == ' ' }) {
                            name = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Enter your Name",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = modifier.height(20.dp))
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

                        modifier = modifier
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                    ) {
                        values.forEach { label ->
                            DropdownMenuItem(
                                text = {
                                    Text(label, style = MaterialTheme.typography.bodyLarge)
                                },
                                onClick = {
                                    currentVal = label
                                    isExpanded = false
                                },
                                modifier = modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                Spacer(modifier = modifier.height(20.dp))
                if(currentVal == "Personal"){
                    OutlinedTextField(
                        value = vpa,
                        onValueChange = {
                                vpa = it
                        },
                        placeholder = {
                            Text(
                                text = "Enter your VPA",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Payment,
                                contentDescription = null
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = modifier.height(20.dp))
                }
                Button(onClick = {
                    if(currentVal=="Merchant"){
                        onUserEvents(UserEvents.AddUser(user = UserModel(name=name, vpa = "", isMerchant = currentVal == "Merchant")))
                    }
                    else{
                        onUserEvents(UserEvents.AddUser(user = UserModel(name=name, vpa = vpa, isMerchant = currentVal == "Merchant")))
                    }

                }) {
                    Text(
                        "Get Started",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                Bottom()
            }
        }
    }
}
