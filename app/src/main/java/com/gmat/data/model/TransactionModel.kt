package com.gmat.data.model

import com.google.firebase.Timestamp

data class TransactionModel(
    val timestamp: Timestamp,
    val payeeId: String="",
    val payerId: String="",
    val txnId: String="",
    val amount: String,
    val type: Int=0,
    val status: Int=1, // complete
    val gstin: String=""
)