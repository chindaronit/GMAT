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
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userAPI: UserAPI
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state = _state.asStateFlow()

    fun onEvent(event: UserEvents) {
        when(event){
            is UserEvents.AddUser -> {
                addUser(event.user)
            }
            is UserEvents.GetUserByPhone -> {
                getUserByPhone(event.phNo)
            }
            is UserEvents.GetUserByUserId -> {
                getUserByUserId(event.userId)
            }
            is UserEvents.GetUserByVPA -> {
                getUserByVPA(event.vpa)
            }
            is UserEvents.UpdateUser ->{
                updateUser(event.user)
            }

            UserEvents.SignOut -> {
                _state.update { it.copy(phNo = "", user = null) }
            }

            is UserEvents.ChangePhNo -> {
                _state.update { it.copy(phNo=event.phNo) }
            }

            UserEvents.SignIn -> {
                getUserByPhone(state.value.phNo)
            }

            is UserEvents.ChangeVerificationId -> {
                _state.update { it.copy(verificationId = event.id) }
            }
        }
    }

    private fun getUserByUserId(userId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = userAPI.getUserByUserId(userId)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("GetUserByUserId", "user: ${response.body()}")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = response.body()
                        )
                    }
                } else {
                    // Handle different status codes
                    when (response.code()) {
                        403 -> {
                            Log.d("GetUserByUserId", "Forbidden: 403")
                            _state.update { it.copy(isLoading = false, error = "Forbidden: Access denied") }
                        }
                        404 -> {
                            Log.d("GetUserByUserId", "Not Found: 404")
                            _state.update { it.copy(isLoading = false, error = "User not found")}
                        }
                        else -> {
                            println(response.code())
                            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                            _state.update { it.copy(isLoading = false, error = errorObj.getString("message")) }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Check Your Internet Connection") }
            }
        }
    }

    private fun getUserByPhone(phNo: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = userAPI.getUserByPhone(phNo)
                println(response)
                if (response.isSuccessful && response.body()!=null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = response.body()
                        )
                    }
                } else {
                    // Handle different status codes
                    when (response.code()) {
                        403 -> {
                            Log.d("GetUserByUserPh", "Forbidden: 403")
                            _state.update { it.copy(isLoading = false, error = "Forbidden: Access denied") }
                        }
                        404 -> {
                            Log.d("GetUserByUserPh", "Not Found: 404")
                            _state.update { it.copy(isLoading = false, error = "User not found", user = UserModel()) }
                        }
                        else -> {
                            println(response.code())
                            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                            _state.update { it.copy(isLoading = false, error = errorObj.getString("message")) }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Check Your Internet Connection") }
            }
        }
    }

    private fun getUserByVPA(vpa: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = userAPI.getUserByVPA(vpa)
                if (response.isSuccessful && response.body()!=null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = response.body()
                        )
                    }
                } else {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    _state.update { it.copy(isLoading = false, error = errorObj.getString("message")) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Check Your Internet Connection") }
            }
        }
    }

    private fun addUser(user: UserModel) {
        val phNo = state.value.phNo
        user.phNo=phNo
        viewModelScope.launch {
            try {
                val response = userAPI.addUser(user)
                if (response.isSuccessful) {
                    println("User added successfully")
                    getUserByPhone(phNo) // Fetch user details after adding
                } else {
                    // Log the error response
                    Log.e("AddUser", "Error: ${response.code()}, Message: ${response.message()}")
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    _state.update { it.copy(isLoading = false, error = errorObj.getString("message")) }
                }
            } catch (e: Exception) {
                Log.e("AddUser", "Exception: ${e.message}")
                _state.update { it.copy(isLoading = false, error = "Check Your Internet Connection") }
            }
        }
    }


    private fun updateUser(user: UserModel) {
        val userId= user.userId
        viewModelScope.launch {
            val response=userAPI.updateUser(user)
            if (response.isSuccessful) {
                getUserByPhone(userId)
            } else {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _state.update { it.copy(isLoading = false, error = errorObj.getString("message")) }
            }
        }
    }
}