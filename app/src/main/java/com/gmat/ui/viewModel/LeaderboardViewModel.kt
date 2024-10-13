package com.gmat.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmat.data.repository.api.LeaderboardAPI
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

    fun onEvent(event: LeaderboardEvents) {
        when (event) {
            is LeaderboardEvents.GetUserRewardsPointsForMonth -> {
                getUserRewardsPointsForMonth(event.userId, event.month, event.year)
            }

            is LeaderboardEvents.GetAllUsersByRewardsForMonth -> {
                getAllUsersByRewardsForMonth(event.month, event.year)
            }

            is LeaderboardEvents.AddUserTransactionRewards -> {
                addUserTransactionRewards(event.userId, event.transactionAmount)
            }
        }
    }

    private fun getUserRewardsPointsForMonth(userId: String, month: Int, year: Int) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = leaderboardAPI.getUserRewardsPointsForMonth(userId, month, year)
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

    private fun getAllUsersByRewardsForMonth(month: Int, year: Int) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = leaderboardAPI.getUsersByRewardsForMonth(month, year)
                if (response.isSuccessful && response.body() != null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            allEntries = response.body()!!
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

    private fun addUserTransactionRewards(userId: String, transactionAmount: String) {
        viewModelScope.launch {
            try {
                val response = leaderboardAPI.updateUserTransactionRewards(
                    TransactionRequest(
                        userId = userId,
                        transactionAmount = transactionAmount.toInt()
                    )
                )
                if (!response.isSuccessful) {
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
            401 -> "Unauthorized: Access denied"
            404 -> "Not Found: Resource not found"
            500 -> "Internal Server Error: Please try again later"
            else -> {
                // Attempt to parse the error message from the response body
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                errorObj.getString("message")
            }
        }
        Log.e("LeaderboardViewModel", "Error: $errorMessage")
        _state.update { it.copy(isLoading = false, error = errorMessage) }
    }
}
