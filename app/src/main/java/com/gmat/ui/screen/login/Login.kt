package com.gmat.ui.screen.login

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.gmat.env.formatPhoneNumberForVerification
import com.gmat.env.startPhoneNumberVerification
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.CustomToast
import com.gmat.ui.components.login.Bottom
import com.gmat.ui.components.login.Top
import com.gmat.ui.events.UserEvents
import com.gmat.ui.state.UserState
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    modifier: Modifier = Modifier,
    navController: NavController,
    userState: UserState,
    onUserEvents: (UserEvents) -> Unit
) {

    LaunchedEffect(key1 = userState) {
        if(userState.user!=null){
            navController.navigate(NavRoutes.Home.route)
        }
    }

    LaunchedEffect(key1 = userState.user) {
        if (userState.user != null) {
            println(userState.user)
        }
    }

    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current as Activity
    var isToastVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("GMAT", style = MaterialTheme.typography.headlineLarge) })
        }
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
                    .fillMaxWidth()
                    .padding(40.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Continue with mobile",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = modifier.height(20.dp))
                OutlinedTextField(
                    value = userState.phNo,
                    onValueChange = {
                        if (it.length <= 10 && it.isDigitsOnly()) {
                            onUserEvents(UserEvents.ChangePhNo(it))
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Enter your number here",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = null,
                        )
                    },
                    modifier = modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )

                Button(
                    onClick = {
                        isToastVisible = true
                        startPhoneNumberVerification(
                            phoneNumber = formatPhoneNumberForVerification(
                                userState.phNo
                            ),
                            auth = auth,
                            activity = context,
                            onVerificationFailed = {},
                            onVerificationCompleted = {
                                isToastVisible = false
                                onUserEvents(UserEvents.ChangeVerificationId(it))
                                navController.navigate(NavRoutes.OTP.route)
                            })
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp),
                ) {
                    Text(
                        text = "Continue",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            CustomToast(
                modifier = modifier.align(Alignment.BottomCenter),
                message = "Please wait...",
                isVisible = isToastVisible
            )
            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
            ) {
                Bottom()
            }
        }
    }
}
