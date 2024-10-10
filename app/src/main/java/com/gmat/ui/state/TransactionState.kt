package com.gmat.ui.state

import com.gmat.data.model.TransactionModel


data class TransactionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val transaction: TransactionModel?=null
)
