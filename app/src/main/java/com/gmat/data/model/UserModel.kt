package com.gmat.data.model

data class UserModel(
    val userId: String="",
    val name: String="",
    val vpa: String="",
    val profile: String="",
    var phNo: String="",
    val qr: String="",
    val isMerchant: Boolean=false
)