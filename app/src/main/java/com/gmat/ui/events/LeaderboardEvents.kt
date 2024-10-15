package com.gmat.ui.events


sealed class LeaderboardEvents {
    data class GetUserRewardsPointsForMonth(val userId: String, val month: Int, val year: Int,val authToken: String) : LeaderboardEvents()
    data class GetAllUsersByRewardsForMonth(val month: Int, val year: Int,val authToken: String) : LeaderboardEvents()
    data class AddUserTransactionRewards(val userId: String, val transactionAmount: String,val authToken: String) : LeaderboardEvents()
    data object SignOut: LeaderboardEvents()
}
