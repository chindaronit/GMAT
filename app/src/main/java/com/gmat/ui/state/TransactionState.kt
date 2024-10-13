package com.gmat.ui.state

import com.gmat.data.model.TransactionModel
import com.gmat.env.ChatDetails
import com.gmat.env.TransactionHistory


data class TransactionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val transaction: TransactionModel? = null,
    val recentUserTransactions: List<ChatDetails>?=null,
    val transactionHistory: List<TransactionModel>?=null
)
