package com.gmat.ui.events

import com.gmat.data.model.LeaderboardModel

sealed class LeaderboardEvents {
    data class GetUserRewardsPointsForMonth(val userId: String, val month: Int, val year: Int) : LeaderboardEvents()
    data class GetAllUsersByRewardsForMonth(val month: Int, val year: Int) : LeaderboardEvents()
    data class AddUserTransactionRewards(val userId: String, val transactionAmount: String) : LeaderboardEvents()
}
