package com.example.superdapp.domain.walletconnect

import com.walletconnect.sign.client.Sign
import com.walletconnect.sign.client.SignClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignMessageUseCase @Inject constructor() {

    operator fun invoke(account: String, sessionTopic: String): Flow<SignStatus> =
        callbackFlow {
            trySend(SignStatus.Signing)

            val chainId = "eip155:1"
            val method = "personal_sign"

            val params = getPersonalSignBody(account)

            val expiry =
                (System.currentTimeMillis() / 1000) + TimeUnit.SECONDS.convert(7, TimeUnit.DAYS)

            val requestParams = Sign.Params.Request(sessionTopic, method, params, chainId, expiry)
            val activeSession = checkNotNull(SignClient.getActiveSessionByTopic(sessionTopic))
            Timber.d("WalletConnect Connection is active: $activeSession")

            SignClient.request(requestParams,
                onSuccess = { request: Sign.Model.SentRequest ->
                    Timber.d("WalletConnect SignClient.request Success")
                    trySend(SignStatus.SignRequested)
                },
                onError = { error: Sign.Model.Error ->
                    Timber.e("WalletConnect SignClient.request error $error")
                    trySend(SignStatus.Error(error.toString()))
                }
            )
            awaitClose()
        }

    private fun getPersonalSignBody(account: String): String {
        val msg = "This is my personal message"
        return "[\"$msg\", \"$account\"]"
    }

}