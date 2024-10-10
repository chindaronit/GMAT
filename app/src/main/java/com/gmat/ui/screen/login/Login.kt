package com.gmat.ui.screen.login


import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.gmat.functionality.startPhoneNumberVerification
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.login.Bottom
import com.gmat.ui.components.login.Top
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var number by remember {
        mutableStateOf("")
    }
    val auth= FirebaseAuth.getInstance()
    val context= LocalContext.current as Activity

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("GMAT", fontFamily = FontFamily.Monospace) })
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
                    text = "Continue with Mobile",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = modifier.height(20.dp))
                OutlinedTextField(
                    value = number,
                    onValueChange = {
                        if (it.length <= 10 && it.isDigitsOnly()) {
                            number = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Enter your number here",
                            fontFamily = FontFamily.Monospace
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "",
                        )
                    },
                    modifier = modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )

                Button(
                    onClick = {
                        startPhoneNumberVerification(phoneNumber = number, auth = auth, activity = context){ verificationId ->
                            navController.navigate(NavRoutes.OTP.withArgs(verificationId))
                        }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp),
                ) {
                    Row(
                        modifier = modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Request OTP",
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            }

            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
            ) {
                Bottom()
            }
        }
    }
}
