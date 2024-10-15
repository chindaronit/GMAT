package com.gmat.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.gmat.data.repository.api.LeaderboardAPI
import com.gmat.env.ListLeaderboardResponse
import com.gmat.env.TransactionRequest
import com.gmat.ui.events.LeaderboardEvents
import com.gmat.ui.state.LeaderboardState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardAPI: LeaderboardAPI
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()

    private lateinit var userViewModel: UserViewModel

    // Initialize UserViewModel when needed
    fun initUserViewModel(owner: ViewModelStoreOwner) {
        userViewModel = ViewModelProvider(owner)[UserViewModel::class.java]
    }

    // Example of calling refreshToken() from UserViewModel
    private suspend fun refreshTokenUsingUserViewModel(): String? {
        if (!::userViewModel.isInitialized) {
            return null // Or handle the case when it's not initialized
        }
        return userViewModel.refreshToken()
    }

    fun onEvent(event: LeaderboardEvents) {
        when (event) {
            is LeaderboardEvents.GetUserRewardsPointsForMonth -> {
                getUserRewardsPointsForMonth(event.userId, event.month, event.year, event.authToken)
            }

            is LeaderboardEvents.GetAllUsersByRewardsForMonth -> {
                getAllUsersByRewardsForMonth(event.month, event.year, token = event.authToken)
            }

            is LeaderboardEvents.AddUserTransactionRewards -> {
                addUserTransactionRewards(
                    event.userId,
                    event.transactionAmount,
                    token = event.authToken
                )
            }

            LeaderboardEvents.SignOut -> {
                _state.update {
                    it.copy(
                        userLeaderboardEntry = null,
                        allEntries = ListLeaderboardResponse(),
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    private fun getUserRewardsPointsForMonth(userId: String, month: Int, year: Int, token: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = leaderboardAPI.getUserRewardsPointsForMonth(
                    userId,
                    month,
                    year,
                    token = "Bearer $token"
                )
                if (response.isSuccessful && response.body() != null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userLeaderboardEntry = response.body()!!.rewards
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

    private fun getAllUsersByRewardsForMonth(month: Int, year: Int, token: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response =
                    leaderboardAPI.getUsersByRewardsForMonth(month, year, token = "Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            allEntries = response.body()!!
                        )
                    }
                } else {
                    if (response.code() == 401) {
                        val token = refreshTokenUsingUserViewModel()
                        if (token != null) {
                            getAllUsersByRewardsForMonth(month, year, token=token)
                        }
                    }
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

    private fun addUserTransactionRewards(
        userId: String,
        transactionAmount: String,
        token: String
    ) {
        viewModelScope.launch {
            try {
                val response = leaderboardAPI.updateUserTransactionRewards(
                    TransactionRequest(
                        userId = userId,
                        transactionAmount = transactionAmount.toInt(),
                    ), token = "Bearer $token"
                )
                if (!response.isSuccessful) {
                    handleErrorResponse(response)
                }
                else{
                    if (response.code() == 401) {
                        val token = refreshTokenUsingUserViewModel()
                        if (token != null) {
                            addUserTransactionRewards(userId, transactionAmount, token)
                        }
                    }
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

    private fun handleErrorResponse(response: Response<*>) {
        val errorMessage = when (response.code()) {
            400 -> "Bad Request: Please check the input data"
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
