package com.gmat.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmat.data.model.UserModel
import com.gmat.data.repository.api.UserAPI
import com.gmat.ui.events.UserEvents
import com.gmat.ui.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userAPI: UserAPI
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state = _state.asStateFlow()

    fun onEvent(event: UserEvents) {
        when (event) {
            is UserEvents.AddUser -> {
                addUser(event.user)
            }

            is UserEvents.GetUserByPhone -> {
                getUserByPhone(event.phNo)
            }

            is UserEvents.GetUserByUserId -> {
                getUserByUserId(event.userId)
            }

            is UserEvents.UpdateUser -> {
                updateUser()
            }

            UserEvents.SignOut -> {
                _state.update { it.copy(phNo = "", user = null) }
            }

            is UserEvents.ChangePhNo -> {
                _state.update { it.copy(phNo = event.phNo) }
            }

            UserEvents.SignIn -> {
                getUserByPhone(_state.value.phNo)
            }

            is UserEvents.ChangeVerificationId -> {
                _state.update { it.copy(verificationId = event.id) }
            }

            is UserEvents.OnNameChange -> {
                _state.update { it.copy(newName = event.name) }
            }

            is UserEvents.OnProfileChange -> {
                _state.update { it.copy(newProfile = event.profile) }
            }

            is UserEvents.OnChangeQR -> {
                _state.update { it.copy(newQr = event.qr) }
            }

            is UserEvents.OnChangeVPA -> {
                _state.update { it.copy(newVpa= event.vpa) }
            }
        }
    }

    private fun getUserByUserId(userId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = userAPI.getUserByUserId(userId)
                if (response.isSuccessful && response.body() != null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = response.body()
                        )
                    }
                } else {
                    handleErrorResponse(response)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Check Your Internet Connection"
                    )
                }
            }
        }
    }

    private fun getUserByPhone(phNo: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = userAPI.getUserByPhone(phNo)
                if (response.isSuccessful && response.body() != null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = response.body()
                        )
                    }
                } else {
                   handleErrorResponse(response)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Check Your Internet Connection"
                    )
                }
            }
        }
    }

    private fun addUser(user: UserModel) {
        val phNo = state.value.phNo
        user.phNo = phNo
        viewModelScope.launch {
            try {
                val response = userAPI.addUser(user)
                if (response.isSuccessful) {
                    getUserByPhone(phNo) // Fetch user details after adding
                } else {
                    handleErrorResponse(response)
                }
            } catch (e: Exception) {
                Log.e("AddUser", "Exception: ${e.message}")
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Check Your Internet Connection"
                    )
                }
            }
        }
    }


    private fun updateUser() {
        val updatedUser = _state.value.user!!
        updatedUser.profile = _state.value.newProfile.ifBlank { _state.value.user!!.profile }
        updatedUser.name = _state.value.newName.ifBlank { _state.value.user!!.name }
        updatedUser.qr = _state.value.newQr.ifBlank { _state.value.user!!.qr }
        updatedUser.vpa = _state.value.newVpa.ifBlank { _state.value.user!!.vpa }
        val userId = updatedUser.userId
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val response = userAPI.updateUser(updatedUser)
            if (response.isSuccessful) {
                getUserByUserId(userId)
            } else {
                handleErrorResponse(response)
            }
        }
    }

    private fun handleErrorResponse(response: Response<*>) {
        val errorMessage = when (response.code()) {
            400 -> "Bad Request: Please check the input data"
            401 -> "Unauthorized: Access denied"
            404 -> "Not Found: Resource not found"
            500 -> "Internal Server Error: Please try again later"
            else -> {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                errorObj.getString("message")
            }
        }
        Log.e("UserViewModel", "Error: $errorMessage")
        _state.update { it.copy(isLoading = false, error = errorMessage) }
    }
}