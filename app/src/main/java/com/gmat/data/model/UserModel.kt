package com.gmat.data.model

data class UserModel(
    val userId: String="",
    var name: String="",
    val vpa: String="",
    var profile: String="",
    var phNo: String="",
    var qr: String="",
    val isMerchant: Boolean=false
)