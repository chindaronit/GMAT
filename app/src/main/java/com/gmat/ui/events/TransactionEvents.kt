package com.gmat.ui.events

import com.gmat.data.model.TransactionModel

sealed class TransactionEvents {
    data class AddTransaction(val userId: String,val transaction: TransactionModel) : TransactionEvents()
    data class GetTransactionById(val txnId: String, val userId: String) : TransactionEvents()
    data class GetAllTransactionsForMonth(val month: Int, val year: Int, val userId: String?,val vpa: String?) : TransactionEvents()
    data class GetRecentTransactions(val userId: String?,val vpa: String?): TransactionEvents()
    data object ClearTransactionHistory: TransactionEvents()
}
