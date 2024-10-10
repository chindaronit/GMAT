package com.gmat.ui.viewModel

import androidx.lifecycle.ViewModel
import com.gmat.data.repository.api.LeaderboardAPI
import com.gmat.ui.events.LeaderboardEvents
import com.gmat.ui.state.LeaderboardState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardAPI: LeaderboardAPI
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()

    fun onEvent(event: LeaderboardEvents) {
        when(event){
            is LeaderboardEvents.AddUserTransactionRewards -> TODO()
            is LeaderboardEvents.GetAllUsersByRewardsForMonth -> TODO()
            is LeaderboardEvents.GetLeaderboardThroughUserId -> TODO()
            is LeaderboardEvents.GetUserRewardsPointsForMonth -> TODO()
            is LeaderboardEvents.UpdateLeaderboard -> TODO()
        }
    }

}