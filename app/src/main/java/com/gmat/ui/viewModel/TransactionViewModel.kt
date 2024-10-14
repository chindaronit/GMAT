package com.gmat.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmat.data.model.TransactionModel
import com.gmat.data.repository.api.TransactionAPI
import com.gmat.ui.events.LeaderboardEvents
import com.gmat.ui.events.TransactionEvents
import com.gmat.ui.state.TransactionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionAPI: TransactionAPI
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionState())
    val state = _state.asStateFlow()

    fun onEvent(event: TransactionEvents) {
        when (event) {
            is TransactionEvents.AddTransaction -> {
                addTransaction(event.userId, event.transaction)
            }

            is TransactionEvents.GetTransactionById -> {
                getTransactionById(event.userId, event.txnId)
            }

            is TransactionEvents.GetAllTransactionsForMonth -> {
                getAllTransactionsForMonth(event.month, event.year, event.userId, event.vpa)
            }

            is TransactionEvents.GetRecentTransactions -> {
                getRecentTransactions(event.userId, event.vpa)
            }

            TransactionEvents.ClearTransactionHistory -> {
                _state.update { it.copy(transactionHistory =null) }
            }

            TransactionEvents.SignOut -> {
                _state.update { it.copy(isLoading = false, error = null, transaction = null, transactionHistory = null, recentUserTransactions = null) }
            }
        }
    }

    private fun addTransaction(userId: String, transaction: TransactionModel) {
        viewModelScope.launch {
            try {
                val response =
                    transactionAPI.addTransaction(userId = userId, transaction = transaction)

                if (response.isSuccessful && response.body() != null) {
                    transaction.txnId = response.body()!!.txnId
                    _state.update {
                        it.copy(
                            transaction = transaction
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
                Log.e("TransactionViewModel", "Error: ${e.message}")
            }
        }
    }

    private fun getTransactionById(userId: String, txnId: String) {
        _state.update { it.copy(isLoading = true) }
        println("$userId, $txnId")
        viewModelScope.launch {
            try {
                val response = transactionAPI.getTransactionByTxnId(userId = userId, txnId = txnId)
                if (response.isSuccessful && response.body() != null) {
                    println(response.body())
                    _state.update {
                        it.copy(
                            isLoading = false,
                            transaction = response.body()!!.transaction // Update the transaction in state
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
                Log.e("TransactionViewModel", "Error: ${e.message}")
            }
        }
    }


    private fun getAllTransactionsForMonth(month: Int, year: Int, userId: String?, vpa: String?) {
        _state.update { it.copy(isLoading = true) }
        println("$month,$year")
        if(userId==null && vpa!=null){
            viewModelScope.launch {
                try {
                    val response = transactionAPI.getTransactionHistoryForMerchant(vpa=vpa, month = month, year=year)

                    if (response.isSuccessful && response.body() != null) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                transactionHistory = response.body()!!.transactions // Update transactions in state
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
                    Log.e("TransactionViewModel", "Error: ${e.message}")
                }
            }
        }
        else if(userId!=null){
            viewModelScope.launch {
                try {
                    val response = transactionAPI.getAllTransactionsForMonth(month, year, userId)

                    if (response.isSuccessful && response.body() != null) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                transactionHistory = response.body()!!.transactions // Update transactions in state
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
                    Log.e("TransactionViewModel", "Error: ${e.message}")
                }
            }
        }
        else return
    }


    private fun getRecentTransactions(userId: String?, vpa: String?) {
        _state.update { it.copy(isLoading = true) }

        if (vpa == null && userId != null) {
            viewModelScope.launch {
                try {
                    val response = transactionAPI.getRecentTransactionsForUser(userId)

                    if (response.isSuccessful && response.body() != null) {
                        Log.d(
                            "GetRecentTransactionForUser",
                            "Transactions retrieved: ${response.body()}"
                        )
                        _state.update {
                            it.copy(
                                isLoading = false,
                                recentUserTransactions = response.body()!!.data // Update transactions in state
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
                    Log.e("TransactionViewModel", "Error: ${e.message}")
                }
            }
        } else if (vpa != null) {
            viewModelScope.launch {
                try {
                    val response = transactionAPI.getRecentTransactionsForMerchant(vpa)

                    if (response.isSuccessful && response.body() != null) {
                        Log.d(
                            "GetRecentTransactionForMerchant",
                            "Transactions retrieved: ${response.body()}"
                        )
                        _state.update {
                            it.copy(
                                isLoading = false,
                                recentUserTransactions = response.body()!!.data // Update transactions in state
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
                    Log.e("TransactionViewModel", "Error: ${e.message}")
                }
            }
        } else return
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
        Log.e("TransactionViewModel", "Error: $errorMessage")
        _state.update { it.copy(isLoading = false, error = errorMessage) }
    }
}
