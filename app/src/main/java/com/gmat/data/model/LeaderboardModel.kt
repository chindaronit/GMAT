package com.gmat.data.model

import java.time.Month
import java.time.Year

data class LeaderboardModel(
    val userId: String="",
    val name: String,
    val month: String,
    val year: String,
    val points: String=""
)