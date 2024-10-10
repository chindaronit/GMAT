package com.gmat.ui.events

import com.gmat.data.model.UserModel

sealed class UserEvents {
    data class GetUserByPhone(val phNo: String) : UserEvents()
    data class GetUserByVPA(val vpa: String) : UserEvents()
    data class GetUserByUserId(val userId: String) : UserEvents()
    data class AddUser(val user: UserModel) : UserEvents()
    data class UpdateUser(val user: UserModel) : UserEvents()
}
