package com.example.superdapp.ui.home

import androidx.lifecycle.viewModelScope
import com.example.superdapp.domain.ConnectStatus
import com.example.superdapp.domain.ConnectWalletUseCase
import com.example.superdapp.domain.CreatePairingUseCase
import com.example.superdapp.domain.SessionStatus
import com.example.superdapp.domain.SignClientDelegate
import com.example.superdapp.domain.SignMessageUseCase
import com.example.superdapp.domain.SignStatus
import com.example.superdapp.ui.main.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(
    private val createPairing: CreatePairingUseCase,
    private val signClientDelegate: SignClientDelegate,
    private val connectWallet: ConnectWalletUseCase,
    private val signMessage: SignMessageUseCase
) : BaseViewModel<ConnectContract.State, ConnectContract.Event, ConnectContract.SideEffect>(
    ConnectContract.State()
) {

    fun init() = viewModelScope.launch {
        setState {
            val pairing = createPairing()
            copy(
                pairing = pairing,
                pairingUri = createPairing.getUri(pairing)
            )
        }

        signClientDelegate().collect {
            when (it) {
                is SessionStatus.OnApproved -> {
                    setState {
                        copy(
                            approvedSession = it.approvedSession,
                            connectStatus = ConnectStatus.Connected
                        )
                    }
                }

                is SessionStatus.OnSessionRequestResponse -> {
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
        val session = state.value.approvedSession ?: return@launch

        signMessage(session).collect {
            setState { copy(signStatus = it) }
        }
    }

}