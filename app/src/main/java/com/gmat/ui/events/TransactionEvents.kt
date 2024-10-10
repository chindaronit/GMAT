package com.gmat.ui.events

import com.gmat.data.model.TransactionModel

sealed class TransactionEvents {
    data class AddTransaction(val transaction: TransactionModel) : TransactionEvents()
    data class GetTransactionById(val txnId: String) : TransactionEvents()
    data class GetAllTransactionsForUser(val userId: String) : TransactionEvents()
    data class GetAllTransactionsForMonth(val month: Int, val year: Int) : TransactionEvents()
    data class GetUserTransactionsByUserIdAndPayeeId(val userId: String, val payeeId: String) : TransactionEvents()
    data class GetAllTransactionsForPayerIdAndPayeeId(val payerId: String, val payeeId: String) : TransactionEvents()
}
