package com.gmat.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

            }
        }
    }

    private fun getUserByUserId(userId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = userAPI.getUserByUserId(userId)
                println(response)
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
                            _state.update { it.copy(isLoading = false, error = "User not found") }
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
}