package com.example.superdapp.ui.home

import android.net.Uri
import com.example.superdapp.domain.ConnectStatus
import com.example.superdapp.domain.SignStatus
import com.example.superdapp.ui.main.UiEvent
import com.example.superdapp.ui.main.UiSideEffect
import com.example.superdapp.ui.main.UiState
import com.walletconnect.android.Core
import com.walletconnect.sign.client.Sign

class ConnectContract {

    data class State(
        val pairing: Core.Model.Pairing? = null,
        val pairingUri: Uri? = null,
        val approvedSession: Sign.Model.ApprovedSession? = null,
        val connectStatus: ConnectStatus = ConnectStatus.Disconnected,
        val signStatus: SignStatus = SignStatus.Unsigned,
    ) : UiState

    sealed class Event : UiEvent {
        data object OnConnectClick : Event()
        data object OnSignClick : Event()
    }

    sealed class SideEffect : UiSideEffect {
        data class ShowError(val error: String) : SideEffect()
    }
}