package com.gmat.ui.events

import com.gmat.data.model.TransactionModel

sealed class TransactionEvents {
    data class AddTransaction(
        val userId: String,
        val transaction: TransactionModel,
        var token: String?
    ) : TransactionEvents()

    data class GetTransactionById(val txnId: String, val userId: String, var token: String?) :
        TransactionEvents()

    data class GetAllTransactionsForMonth(
        val month: Int,
        val year: Int,
        val userId: String?,
        val vpa: String?,
        var token: String?
    ) : TransactionEvents()

    data class GetRecentTransactions(val userId: String?, val vpa: String?, var token: String?) :
        TransactionEvents()
    data object ClearTransaction: TransactionEvents()
    data object ClearTransactionHistory : TransactionEvents()
    data object SignOut : TransactionEvents()
}
