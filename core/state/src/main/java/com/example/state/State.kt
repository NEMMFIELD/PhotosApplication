package com.example.state

sealed class State<out T> {
    data object Empty : State<Nothing>()
    data class Success<out T>(val data: T) : State<T>() {
        override fun equals(other: Any?): Boolean {
            return other is Success<*> && other.data == data
        }

        override fun hashCode(): Int {
            return data.hashCode()
        }
    }

    data class Failure<out T>(val message: Throwable) : State<T>()
}
