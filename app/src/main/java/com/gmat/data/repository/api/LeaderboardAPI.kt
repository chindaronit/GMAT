package com.gmat.data.repository.api

import com.gmat.data.model.LeaderboardModel
import com.gmat.env.Status
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardAPI {
    // Update user transaction rewards
    @POST("/leaderboard")
    suspend fun updateUserTransactionRewards(
        @Query("userId") userId: String,
        @Query("transactionAmount") transactionAmount: String,
    ): Status<Unit>

    // Get rewards points for a specific month for a user
    @GET("/leaderboard")
    suspend fun getUserRewardsPointsForMonth(
        @Query("userId") userId: String,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<LeaderboardModel>

    // Get all users' rewards points for a specific month
    @GET("/leaderboard/all")
    suspend fun getUsersByRewardsForMonth(
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<List<LeaderboardModel>>
}
