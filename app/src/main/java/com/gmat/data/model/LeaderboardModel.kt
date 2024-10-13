package com.gmat.data.model


data class LeaderboardModel(
    val userId: String="",
    val name: String,
    val month: String,
    val year: String,
    val points: Int=0
)