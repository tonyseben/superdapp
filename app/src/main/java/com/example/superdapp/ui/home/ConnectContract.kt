package com.example.superdapp.ui.home

import com.example.superdapp.domain.walletconnect.ConnectStatus
import com.example.superdapp.domain.walletconnect.DisconnectStatus
import com.example.superdapp.domain.walletconnect.SignStatus
import com.example.superdapp.ui.main.UiEvent
import com.example.superdapp.ui.main.UiSideEffect
import com.example.superdapp.ui.main.UiState

class ConnectContract {

    data class State(
        val pairingUrl: String? = null,
        val connectedWallet: String? = null,
        val sessionTopic: String? = null,
        val accounts: List<String> = emptyList(),

        val connectStatus: ConnectStatus = ConnectStatus.Default,
        val signStatus: SignStatus = SignStatus.Default,
        val disConnectStatus: DisconnectStatus = DisconnectStatus.Default
    ) : UiState

    sealed class Event : UiEvent {
        data object OnConnectClick : Event()
        data object OnSignClick : Event()
        data object OnDisconnectClick : Event()
        data object OnCancelClick : Event()
    }

    sealed class SideEffect : UiSideEffect {
        data class ShowError(val error: String) : SideEffect()
    }
}