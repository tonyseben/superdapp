package com.example.superdapp.domain.walletconnect

sealed class DisconnectStatus {
    data object Default : DisconnectStatus()
    data object Disconnected : DisconnectStatus()
    data class Error(val error: String) : DisconnectStatus()
}