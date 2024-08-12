package com.example.superdapp.domain

sealed class SignStatus {
    data object Unsigned : SignStatus()
    data object Signing : SignStatus()
    data object SignRequested : SignStatus()
    data object Signed : SignStatus()
    data class Error(val message: String) : SignStatus()

}