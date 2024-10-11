package com.gmat.ui.screen.login

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gmat.functionality.signInWithPhoneAuthCredential
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.login.Bottom
import com.gmat.ui.components.login.OtpTextField
import com.gmat.ui.components.login.Top
import com.gmat.ui.events.UserEvents
import com.gmat.ui.state.UserState
import com.google.firebase.auth.PhoneAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTP(
    modifier: Modifier = Modifier,
    navController: NavController,
    userState: UserState,
    onUserEvents: (UserEvents) -> Unit
) {
    var otpValue by remember { mutableStateOf("") }
    val context = LocalContext.current as Activity

    LaunchedEffect(key1 = userState.user) {
        userState.user?.let { user ->
            if (user.phNo.isNotBlank()) {
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(0) { inclusive = true } // This removes everything from the backstack
                    launchSingleTop = true
                }

            } else {
                // Navigate to Register and pop the backstack
                navController.navigate(NavRoutes.Register.route){
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
                title = { Text("GMAT") }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = modifier.align(Alignment.TopCenter)
            ) {
                Top()
            }

            Column(
                modifier = modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter OTP",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = modifier.height(20.dp))
                OtpTextField(
                    otpText = otpValue,
                    onOtpTextChange = { value, _ -> otpValue = value }
                )
                Spacer(modifier = modifier.height(30.dp))
                Button(
                    onClick = {
                        if (otpValue.length == 6) {
                            if (userState.verificationId.isNotEmpty() && otpValue.isNotEmpty()) {
                                val credential =
                                    PhoneAuthProvider.getCredential(userState.verificationId, otpValue)
                                signInWithPhoneAuthCredential(
                                    credential,
                                    activity = context,
                                    onFailure = {},
                                    onSuccess = { onUserEvents(UserEvents.SignIn) })
                            } else {
                                Log.w("Login", "Verification ID or OTP is empty.")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        "Verify",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Column(
                modifier = modifier.align(Alignment.BottomCenter)
            ) {
                Bottom()
            }
        }
    }
}
