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
import com.walletconnect.sign.client.Sign
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

                is SessionStatus.OnReject -> reset()

                is SessionStatus.OnSessionRequestResponse -> {
                    when (it.response.result) {
                        is Sign.Model.JsonRpcResponse.JsonRpcResult -> {
                            if (it.response.topic == state.value.sessionTopic) {
                                saveSessionLocal(it.response.topic)
                                setState { copy(signStatus = SignStatus.Signed) }
                            } else {
                                handleError("Sign message failed. Invalid sign due to topic mismatch.")
                                setState { copy(signStatus = SignStatus.Default) }
                            }
                        }

                        is Sign.Model.JsonRpcResponse.JsonRpcError -> {
                            val error =
                                (it.response.result as Sign.Model.JsonRpcResponse.JsonRpcError).message
                            handleError("Sign message failed. $error")
                            setState { copy(signStatus = SignStatus.Default) }
                        }
                    }

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
            ConnectContract.Event.OnCancelClick -> onCancelClick()
        }
    }

    private fun onConnectWalletClick() = viewModelScope.launch {
        val pairing = createPairing()
        connectWallet(pairing).collect { status ->
            setState { copy(connectStatus = status) }
            when (status) {
                is ConnectStatus.ConnectRequested -> setState { copy(pairingUrl = status.pairingUrl) }
                is ConnectStatus.Error -> handleError(status.message, status.throwable)
                else -> {}
            }
        }
    }

    private fun onSignMessageClick() = viewModelScope.launch {
        val account = state.value.accounts.firstOrNull() ?: run {
            handleError("Could not sign message. No account found!")
            return@launch
        }
        val topic = state.value.sessionTopic ?: run {
            handleError("Could not sign message. No session topic found!")
            return@launch
        }

        signMessage(account, topic).collect { status ->
            when (status) {
                is SignStatus.Error -> handleError(status.message, status.throwable)
                else -> setState { copy(signStatus = status) }
            }
        }
    }

    private fun onDisconnectWalletClick() = viewModelScope.launch {
        val topic = state.value.sessionTopic ?: run {
            handleError("Could not disconnect wallet. No session topic found!")
            return@launch
        }

        disconnectWallet(topic).collect { status ->
            when (status) {
                DisconnectStatus.Default -> setState { copy(disConnectStatus = status) }
                DisconnectStatus.Disconnected -> {
                    setState { copy(disConnectStatus = status) }
                    reset()
                }

                is DisconnectStatus.Error -> handleError(status.message, status.throwable)
            }
        }
    }

    private fun onCancelClick() = viewModelScope.launch {
        reset()
    }

    private fun reset() = viewModelScope.launch {
        clearSessionLocal()

        delay(1000)
        setState {
            copy(
                pairingUrl = null,
                connectedWallet = null,
                sessionTopic = null,
                accounts = emptyList(),
                connectStatus = ConnectStatus.Default,
                signStatus = SignStatus.Default,
                disConnectStatus = DisconnectStatus.Default
            )
        }
    }

    private fun handleError(message: String?, throwable: Throwable? = null) {
        throwable?.printStackTrace()
        applySideEffect(
            ConnectContract.SideEffect.ShowError(
                message
                    ?: "Something went wrong. Please try again."
            )
        )
    }
}