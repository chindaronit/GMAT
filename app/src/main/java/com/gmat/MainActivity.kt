package com.gmat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gmat.ui.screen.Login
import com.gmat.ui.screen.OTP
import com.gmat.ui.screen.Profile
import com.gmat.ui.screen.Register
import com.gmat.ui.theme.GMATTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GMATTheme {
//                Profile()
//                OTP()
//                Login()
                Register()
            }
        }
    }
}

