package com.gmat.ui.screen.login

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
import com.gmat.navigation.NavRoutes
import com.gmat.ui.components.login.Bottom
import com.gmat.ui.components.login.OtpTextField
import com.gmat.ui.components.login.Top

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTP(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    var otpValue by remember {
        mutableStateOf("")
    }

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

        ) { innerPadding ->
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
                    .align(Alignment.Center),
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
                    onOtpTextChange = { value, _ ->
                        otpValue = value
                    }
                )
                Spacer(modifier = modifier.height(30.dp))
                Button(
                    onClick = {
                        navController.navigate(NavRoutes.Register.route) {
                            popUpTo(NavRoutes.OTP.route) {
                                inclusive = true
                            } // Clears the back stack
                            launchSingleTop = true  // Avoids multiple instances of the OTP screen
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
                modifier = modifier
                    .align(Alignment.BottomCenter)
            ) {
                Bottom()
            }
        }
    }
}
