package com.example.superdapp.domain

import com.walletconnect.sign.client.Sign
import com.walletconnect.sign.client.SignClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class DisconnectWalletUseCase @Inject constructor() {

    operator fun invoke(topic: String): Flow<DisconnectStatus> = callbackFlow {
        val connectParams = Sign.Params.Disconnect(
            sessionTopic = topic,
        )

        SignClient.disconnect(
            disconnect = connectParams,
            onSuccess = {
                Timber.d("WalletConnect SignClient.disconnect success")
                trySend(DisconnectStatus.Disconnected)
            },
            onError = { error ->
                Timber.d("WalletConnect SignClient.disconnect error: $error")
                trySend(DisconnectStatus.Error(error.toString()))
            }
        )
        awaitClose()
    }
}