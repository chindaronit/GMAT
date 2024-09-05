package com.gmat.ui.components.animations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally


private const val DEFAULT_FADE_DURATION = 300
private const val DEFAULT_SLIDE_DURATION = 400


fun defaultScreenEnterAnimation(): EnterTransition {
    return fadeIn(animationSpec = tween(DEFAULT_FADE_DURATION))
}

fun defaultScreenExitAnimation(): ExitTransition {
    return fadeOut(animationSpec = tween(DEFAULT_FADE_DURATION))
}

fun slideScreenEnterAnimation(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(DEFAULT_SLIDE_DURATION)
    )
}

fun slideScreenExitAnimation(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(DEFAULT_SLIDE_DURATION)
    )
}
