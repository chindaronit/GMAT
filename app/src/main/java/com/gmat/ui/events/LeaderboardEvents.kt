package com.gmat.ui.events

import com.gmat.data.model.LeaderboardModel

sealed class LeaderboardEvents {
    data class GetLeaderboardThroughUserId(val userId: String) : LeaderboardEvents()
    data class UpdateLeaderboard(val leaderboard: LeaderboardModel) : LeaderboardEvents()
}