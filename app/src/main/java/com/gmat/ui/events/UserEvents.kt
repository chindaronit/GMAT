package com.gmat.ui.events

import com.gmat.data.model.UserModel

sealed class UserEvents {
    data object SignOut : UserEvents()
    data class GetUserByPhone(val phNo: String) : UserEvents()
    data class GetUserByVPA(val vpa: String) : UserEvents()
    data class GetUserByUserId(val userId: String) : UserEvents()
    data class AddUser(val user: UserModel) : UserEvents()
    data object UpdateUser: UserEvents()
    data class ChangePhNo(val phNo: String) : UserEvents()
    data object SignIn: UserEvents()
    data class ChangeVerificationId(val id: String) : UserEvents()
    data class OnProfileChange(val profile: String): UserEvents()
    data class OnNameChange(val profile: String): UserEvents()
}
