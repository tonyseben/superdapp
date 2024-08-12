package com.example.superdapp.ui.home

import androidx.lifecycle.viewModelScope
import com.example.superdapp.domain.session.ClearSessionUseCase
import com.example.superdapp.domain.session.GetSessionUseCase
import com.example.superdapp.domain.session.SaveSessionUseCase
import com.example.superdapp.domain.walletconnect.ConnectStatus
import com.example.superdapp.domain.walletconnect.ConnectWalletUseCase
import com.example.superdapp.domain.walletconnect.CreatePairingUseCase
import com.example.superdapp.domain.walletconnect.DisconnectStatus
import com.example.superdapp.domain.walletconnect.DisconnectWalletUseCase
import com.example.superdapp.domain.walletconnect.SessionStatus
import com.example.superdapp.domain.walletconnect.SignClientDelegate
import com.example.superdapp.domain.walletconnect.SignMessageUseCase
import com.example.superdapp.domain.walletconnect.SignStatus
import com.example.superdapp.ui.main.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(
    private val createPairing: CreatePairingUseCase,
    private val signClientDelegate: SignClientDelegate,
    private val connectWallet: ConnectWalletUseCase,
    private val signMessage: SignMessageUseCase,
    private val disconnectWallet: DisconnectWalletUseCase,
    private val getSessionLocal: GetSessionUseCase,
    private val saveSessionLocal: SaveSessionUseCase,
    private val clearSessionLocal: ClearSessionUseCase
) : BaseViewModel<ConnectContract.State, ConnectContract.Event, ConnectContract.SideEffect>(
    ConnectContract.State()
) {

    fun init() = viewModelScope.launch {
        getSessionLocal()?.let { session ->
            setState {
                copy(
                    accounts = session.namespaces["eip155"]?.accounts.orEmpty(),
                    sessionTopic = session.topic,
                    connectedWallet = session.metaData?.name,
                    connectStatus = ConnectStatus.Connected,
                    signStatus = SignStatus.Signed
                )
            }
        }

        setState {
            val pairing = createPairing()
            copy(
                pairing = pairing,
                pairingUri = createPairing.getUri(pairing)
            )
        }

        signClientDelegate().collect { it ->
            when (it) {
                is SessionStatus.OnApproved -> {
                    it.approvedSession.let { session ->
                        setState {
                            copy(
                                accounts = session.accounts,
                                sessionTopic = session.topic,
                                connectedWallet = session.metaData?.name,
                                connectStatus = ConnectStatus.Connected
                            )
                        }
                    }
                }

                is SessionStatus.OnSessionRequestResponse -> {
                    state.value.sessionTopic?.let { topic -> saveSessionLocal(topic) }
                    setState { copy(signStatus = SignStatus.Signed) }
                }

                is SessionStatus.Error -> {
                    applySideEffect(ConnectContract.SideEffect.ShowError(it.error))
                }
            }
        }
    }

    override fun handleEvents(event: ConnectContract.Event) {
        when (event) {
            is ConnectContract.Event.OnConnectClick -> onConnectWalletClick()
            is ConnectContract.Event.OnSignClick -> onSignMessageClick()
            is ConnectContract.Event.OnDisconnectClick -> onDisconnectWalletClick()
        }
    }

    private fun onConnectWalletClick() = viewModelScope.launch {
        state.value.pairing?.let {
            connectWallet(it).collect {
                setState { copy(connectStatus = it) }
            }
        }
    }

    private fun onSignMessageClick() = viewModelScope.launch {
        val account = state.value.accounts.firstOrNull() ?: return@launch
        val topic = state.value.sessionTopic ?: return@launch

        signMessage(account, topic).collect {
            setState { copy(signStatus = it) }
        }
    }

    private fun onDisconnectWalletClick() = viewModelScope.launch {
        val topic = state.value.sessionTopic ?: return@launch

        disconnectWallet(topic).collect {
            setState { copy(disConnectStatus = it) }
            clearSessionLocal()

            if (it is DisconnectStatus.Disconnected) {
                delay(1000)
                setState {
                    copy(
                        connectStatus = ConnectStatus.Default,
                        signStatus = SignStatus.Default,
                        disConnectStatus = DisconnectStatus.Default
                    )
                }
            }
        }
    }
}