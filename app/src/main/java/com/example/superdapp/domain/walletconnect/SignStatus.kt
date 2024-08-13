package com.example.superdapp.domain.walletconnect

sealed class SignStatus {
    data object Default : SignStatus()
    data object Signing : SignStatus()
    data object SignRequested : SignStatus()
    data object Signed : SignStatus()
    data class Error(val message: String, val throwable: Throwable) : SignStatus()

}