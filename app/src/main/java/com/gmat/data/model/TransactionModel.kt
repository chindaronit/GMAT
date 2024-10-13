package com.gmat.data.model

import com.google.firebase.Timestamp

data class TransactionModel(
    val timestamp: Timestamp=Timestamp.now(),
    val payeeId: String="",
    val payerId: String="",
    var txnId: String="",
    val amount: String="0",
    val type: Int=0,
    val status: Int=1, // complete
    val gstin: String="",
    val name: String="",
    val payerUserId: String=""
)