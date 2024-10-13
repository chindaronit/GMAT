package com.gmat.env

import com.gmat.data.model.LeaderboardModel
import com.gmat.data.model.TransactionModel
import com.gmat.data.model.UserModel

data class ListLeaderboardResponse(
    val data: List<LeaderboardModel> = emptyList()
)

data class UserLeaderboardResponse(
    val message: String="",
    val rewards: LeaderboardModel
)

data class ReceiptTransaction(
    val message: String="",
    val transaction: TransactionModel
)

data class ChatDetails (
    val userDetails: UserModel?=null,
    val transactions: List<TransactionModel> = emptyList()
)

data class RecentUserTransactions(
    val message: String="",
    val data: List<ChatDetails> = emptyList()
)

data class TransactionHistory(
    val message: String="",
    val transactions: List<TransactionModel> = emptyList()
)

data class AddTransactionResponse(
    val msg: String="",
    val txnId: String
)

data class TransactionRequest(
    val userId: String,
    val transactionAmount: Int
)