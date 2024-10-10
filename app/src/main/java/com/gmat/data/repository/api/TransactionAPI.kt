package com.gmat.data.repository.api

import com.gmat.data.model.TransactionModel
import com.gmat.env.Status
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TransactionAPI {
    // Get a transaction by its transaction ID
    @GET("/transactions")
    suspend fun getTransactionByTxnId(
        @Query("txnId") txnId: String
    ): Response<TransactionModel>

    // Add a new transaction
    @POST("/transactions")
    suspend fun addTransaction(
        @Query("payerId") payerId: String,
        @Query("payeeId") payeeId: String,
        @Query("amount") amount: Double,
        @Query("gstin") gstin: String? = null
    ): Status<Unit>

    // Get all transactions for a specific user
    @GET("/transactions/all/user")
    suspend fun getAllTransactionsForUser(
        @Query("userId") userId: String
    ): Response<List<TransactionModel>>

    // Get all transactions for a specific month
    @GET("/transactions/all/month")
    suspend fun getAllTransactionsForMonth(
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<List<TransactionModel>>

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
    ): Response<List<TransactionModel>>
}
