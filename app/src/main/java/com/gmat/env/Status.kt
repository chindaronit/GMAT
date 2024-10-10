package com.gmat.env

sealed class Status<out T : Any> {
    data class Success<out T : Any>(val data: T) : Status<T>()
    data class Failure(val exception: Exception) : Status<Nothing>()
}