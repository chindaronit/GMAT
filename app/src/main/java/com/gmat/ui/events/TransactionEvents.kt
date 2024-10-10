package com.gmat.ui.events

import com.gmat.data.model.TransactionModel


sealed class TransactionEvents {
    data class AddTransaction(val transaction: TransactionModel) : TransactionEvents()

}

