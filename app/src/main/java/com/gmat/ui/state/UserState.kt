package com.gmat.ui.state

import com.gmat.data.model.UserModel

data class UserState (
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: UserModel? =null,
    val phNo: String = "",
    val verificationId: String="",
    val newName: String="",
    val newProfile: String="",
    val newQr: String=""
)