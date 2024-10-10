package com.gmat.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmat.data.model.TransactionModel
import com.gmat.data.repository.api.TransactionAPI
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
                addTransaction(event.transaction)
            }
            is TransactionEvents.GetTransactionById -> {
                getTransactionById(event.txnId)
            }
            is TransactionEvents.GetAllTransactionsForUser -> {
                getAllTransactionsForUser(event.userId)
            }
            is TransactionEvents.GetAllTransactionsForMonth -> {
                getAllTransactionsForMonth(event.month, event.year)
            }
            is TransactionEvents.GetUserTransactionsByUserIdAndPayeeId -> {
                getUserTransactionsByUserIdAndPayeeId(event.userId, event.payeeId)
            }
            is TransactionEvents.GetAllTransactionsForPayerIdAndPayeeId -> {
                getAllTransactionsForPayerIdAndPayeeId(event.payerId, event.payeeId)
            }
        }
    }

    private fun addTransaction(transaction: TransactionModel) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = transactionAPI.addTransaction(
                    payerId = transaction.payerId,
                    payeeId = transaction.payeeId,
                    amount = transaction.amount,
                    gstin = transaction.gstin
                )

                if (response.isSuccessful) {
                    Log.d("AddTransaction", "Transaction added successfully")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            transaction = transaction // You can update the transaction state with the new transaction
                        )
                    }
                } else {
                    handleErrorResponse(response)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Check Your Internet Connection") }
                Log.e("TransactionViewModel", "Error: ${e.message}")
            }
        }
    }

    private fun getTransactionById(txnId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = transactionAPI.getTransactionByTxnId(txnId)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("GetTransactionById", "Transaction retrieved: ${response.body()}")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            transaction = response.body() // Update the transaction in state
                        )
                    }
                } else {
                    handleErrorResponse(response)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Check Your Internet Connection") }
                Log.e("TransactionViewModel", "Error: ${e.message}")
            }
        }
    }

    private fun getAllTransactionsForUser(userId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = transactionAPI.getAllTransactionsForUser(userId)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("GetAllTransactionsForUser", "Transactions retrieved: ${response.body()}")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            transactions = response.body()!! // Update transactions in state
                        )
                    }
                } else {
                    handleErrorResponse(response)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Check Your Internet Connection") }
                Log.e("TransactionViewModel", "Error: ${e.message}")
            }
        }
    }

    private fun getAllTransactionsForMonth(month: Int, year: Int) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = transactionAPI.getAllTransactionsForMonth(month, year)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("GetAllTransactionsForMonth", "Transactions retrieved: ${response.body()}")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            transactions = response.body()!! // Update transactions in state
                        )
                    }
                } else {
                    handleErrorResponse(response)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Check Your Internet Connection") }
                Log.e("TransactionViewModel", "Error: ${e.message}")
            }
        }
    }

    private fun getUserTransactionsByUserIdAndPayeeId(userId: String, payeeId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = transactionAPI.getUserTransactionsByUserIdAndPayeeId(userId, payeeId)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("GetUserTransactionsByUserIdAndPayeeId", "Transactions retrieved: ${response.body()}")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            transactions = response.body()!! // Update transactions in state
                        )
                    }
                } else {
                    handleErrorResponse(response)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Check Your Internet Connection") }
                Log.e("TransactionViewModel", "Error: ${e.message}")
            }
        }
    }

    private fun getAllTransactionsForPayerIdAndPayeeId(payerId: String, payeeId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = transactionAPI.getAllTransactionsForPayerIdAndPayeeId(payerId, payeeId)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("GetAllTransactionsForPayerIdAndPayeeId", "Transactions retrieved: ${response.body()}")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            transactions = response.body()!! // Update transactions in state
                        )
                    }
                } else {
                    handleErrorResponse(response)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Check Your Internet Connection") }
                Log.e("TransactionViewModel", "Error: ${e.message}")
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
        Log.e("TransactionViewModel", "Error: $errorMessage")
        _state.update { it.copy(isLoading = false, error = errorMessage) }
    }
}
