package com.gmat.ui.state

import com.gmat.data.model.LeaderboardModel
import com.gmat.env.ListLeaderboardResponse

data class LeaderboardState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userLeaderboardEntry: LeaderboardModel? =null,
    val allEntries: ListLeaderboardResponse=ListLeaderboardResponse()
)
