package com.gmat.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserRoomModel(
    val verificationId: String,
    @PrimaryKey
    val userId: String="",
    var name: String="",
    var vpa: String="",
    var profile: String="",
    var phNo: String="",
    var qr: String="",
    val isMerchant: Boolean=false,
    val authToken: String=""
)