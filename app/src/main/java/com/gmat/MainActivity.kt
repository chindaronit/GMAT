package com.gmat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.gmat.ui.screen.login.Login
import com.gmat.ui.screen.login.OTP
import com.gmat.ui.screen.login.Register
import com.gmat.ui.screen.profile.AboutUs
import com.gmat.ui.screen.profile.FAQ
import com.gmat.ui.screen.profile.Languages
import com.gmat.ui.screen.profile.Profile
import com.gmat.ui.screen.rewards.Rewards
import com.gmat.ui.theme.GMATTheme
import dagger.hilt.android.AndroidEntryPoint


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GMATTheme {
//                Profile()
//                Login()
//                OTP()
//                Register()
//                AboutUs()
                Languages()
//                Rewards()
//                FAQ()
            }
        }
    }
}

