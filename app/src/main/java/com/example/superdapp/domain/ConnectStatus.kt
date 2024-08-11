package com.example.superdapp.domain

sealed class ConnectStatus {
    data object Disconnected : ConnectStatus()
    data object Connecting : ConnectStatus()
    data class ConnectRequested(val pairingUrl: String) : ConnectStatus()
    data class Error(val message: String) : ConnectStatus()
}