package com.gmat.ui.state

import com.gmat.data.model.LeaderboardModel

data class LeaderboardState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val leaderboard: LeaderboardModel? =null
)
