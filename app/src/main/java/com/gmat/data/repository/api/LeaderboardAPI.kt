package com.gmat.data.repository.api


import com.gmat.env.ListLeaderboardResponse
import com.gmat.env.TransactionRequest
import com.gmat.env.UserLeaderboardResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardAPI {
    // Update user transaction rewards
    @POST("/leaderboard")
    suspend fun updateUserTransactionRewards(
        @Body request: TransactionRequest,
        @Header("Authorization") token: String
    ): Response<Unit>

    // Get rewards points for a specific month for a user
    @GET("/leaderboard")
    suspend fun getUserRewardsPointsForMonth(
        @Query("userId") userId: String,
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Header("Authorization") token: String
    ): Response<UserLeaderboardResponse>

    // Get all users' rewards points for a specific month
    @GET("/leaderboard/all")
    suspend fun getUsersByRewardsForMonth(
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Header("Authorization") token: String
    ): Response<ListLeaderboardResponse>
}
