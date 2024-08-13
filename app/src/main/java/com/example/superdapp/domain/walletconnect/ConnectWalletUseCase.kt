package com.example.superdapp.domain.walletconnect

import com.walletconnect.android.Core
import com.walletconnect.sign.client.Sign
import com.walletconnect.sign.client.SignClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class ConnectWalletUseCase @Inject constructor() {

    operator fun invoke(pairing: Core.Model.Pairing): Flow<ConnectStatus> = callbackFlow {
        trySend(ConnectStatus.Connecting)

        val chains = listOf("eip155:1", "eip155:137")
        val methods = listOf(
            "eth_sendTransaction",
            "eth_signTransaction",
            "eth_sign",
            "personal_sign",
            "eth_signTypedData",
            "get_balance"
        )
        val events = listOf("accountsChanged", "chainChanged", "connect", "disconnect")
        val namespaces = mapOf("eip155" to Sign.Model.Namespace.Proposal(chains, methods, events))

        val connectParams = Sign.Params.Connect(
            namespaces = namespaces,
            optionalNamespaces = null,
            properties = null,
            pairing = pairing
        )

        SignClient.connect(
            connect = connectParams,
            onSuccess = { pairingUrl ->
                Timber.d("WalletConnect SignClient.connect success")
                trySend(ConnectStatus.ConnectRequested(pairingUrl))
            },
            onError = { error ->
                Timber.d("WalletConnect SignClient.connect error: $error")
                trySend(
                    ConnectStatus.Error(
                        message = "Could not connect to wallet. Please try again. [SignClient.connect]",
                        throwable = error.throwable
                    )
                )
            }
        )

        awaitClose()
    }
}