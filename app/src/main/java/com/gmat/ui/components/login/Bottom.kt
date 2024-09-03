package com.gmat.ui.components.login

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.gmat.R

@Composable
fun Bottom(){
    Icon(
        painter = painterResource(id = R.drawable.national_emblem),
        contentDescription = "",
    )
}