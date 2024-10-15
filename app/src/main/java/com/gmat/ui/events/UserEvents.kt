package com.gmat.ui.events

import com.gmat.data.model.UserModel

sealed class UserEvents {
    data object SignOut : UserEvents()
    data class GetUserByPhone(val phNo: String) : UserEvents()
    data class GetUserByUserId(val userId: String) : UserEvents()
    data class AddUser(val user: UserModel) : UserEvents()
    data object UpdateUser: UserEvents()
    data class ChangePhNo(val phNo: String) : UserEvents()
    data object SignIn: UserEvents()
    data class ChangeVerificationId(val id: String) : UserEvents()
    data class OnProfileChange(val profile: String): UserEvents()
    data class OnNameChange(val name: String): UserEvents()
    data class OnChangeQR(val qr: String): UserEvents()
    data class OnChangeVPA(val vpa: String): UserEvents()
    data object ClearNewProfile: UserEvents()
    data object SyncUser: UserEvents()
    data class UpdateRoom(val user: UserModel, val verificationId: String,val authToken: String) : UserEvents()
    data object RefreshToken: UserEvents()
    data class OnUpdateAuthToken(val token: String?): UserEvents()
}

