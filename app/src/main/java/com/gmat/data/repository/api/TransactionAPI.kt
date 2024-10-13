package com.gmat.data.repository.api

import com.gmat.data.model.TransactionModel
import com.gmat.env.AddTransactionResponse
import com.gmat.env.ReceiptTransaction
import com.gmat.env.RecentUserTransactions
import com.gmat.env.TransactionHistory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TransactionAPI {
    // Get a transaction by its transaction ID
    // Receipt
    @GET("/transactions")
    suspend fun getTransactionByTxnId(
        @Query("userId") userId: String,
        @Query("txnId") txnId: String
    ): Response<ReceiptTransaction>

    // Add a new transaction
    @POST("/transactions")
    suspend fun addTransaction(
        @Query("userId") userId: String,
        @Body transaction: TransactionModel
    ): Response<AddTransactionResponse>

    // Get all transactions for a specific user
    // History
    @GET("/transactions/all/user")
    suspend fun getAllTransactionsForUser(
        @Query("userId") userId: String
    ): Response<List<TransactionModel>>

    // Get all transactions for a specific month
    @GET("/transactions/all/month")
    suspend fun getAllTransactionsForMonth(
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("userId") userId: String
    ): Response<TransactionHistory>

    // Get all transactions for a user based on userId and payeeId
    @GET("/transactions/all/payee")
    suspend fun getUserTransactionsByUserIdAndPayeeId(
        @Query("userId") userId: String,
        @Query("payeeId") payeeId: String
    ): Response<List<TransactionModel>>

    // Get all transactions between a payer and payee
    @GET("/transactions/all/payerpayee")
    suspend fun getAllTransactionsForPayerIdAndPayeeId(
        @Query("payerId") payerId: String,
        @Query("payeeId") payeeId: String
    ): Response<RecentUserTransactions>

    // Get all transactions between a payer and payee
    // TransactionChat
    @GET("/transactions/recenttransaction")
    suspend fun getRecentTransactionsForUser(
        @Query("userId") userId: String
    ): Response<RecentUserTransactions>

    @GET("/transactions/recentmerchanttransaction")
    suspend fun getRecentTransactionsForMerchant(
        @Query("vpa") vpa: String
    ): Response<RecentUserTransactions>

}
