package com.example.superdapp.domain.walletconnect

sealed class ConnectStatus {
    data object Default : ConnectStatus()
    data object Connecting : ConnectStatus()
    data class ConnectRequested(val pairingUrl: String) : ConnectStatus()
    data object Connected : ConnectStatus()
    data class Error(val message: String) : ConnectStatus()
}