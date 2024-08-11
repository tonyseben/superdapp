package com.example.superdapp.ui.auth

import android.app.Application
import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient
import com.walletconnect.android.relay.ConnectionType
import com.walletconnect.sign.client.Sign
import com.walletconnect.sign.client.SignClient
import timber.log.Timber
import javax.inject.Inject

class WalletConnectAuth @Inject constructor(
    private val application: Application
) : WalletAuth {

    override fun init() {
        val projectId = "18c874cba7064d5a77f6787413a633bc"
        val relayUrl = "relay.walletconnect.com"
        val serverUrl = "wss://$relayUrl?projectId=$projectId"

        CoreClient.initialize(
            relayServerUrl = serverUrl,
            connectionType = ConnectionType.AUTOMATIC,
            application = application,
            metaData = getAppMetadata()
        ) { error ->
            Timber.d("WalletConnect CoreClient INIT FAILED: $error")
        }

        SignClient.initialize(
            init = Sign.Params.Init(CoreClient),
            onSuccess = {
                // Callback will be called if initialization is successful
                Timber.d("WalletConnect SignClient INIT SUCCESS")
            },
            onError = { error ->
                // Error will be thrown if there's an issue during initialization
                Timber.d("WalletConnect SignClient INIT FAILED: $error")
            }
        )
    }

    private fun getAppMetadata() = Core.Model.AppMetaData(
        name = "SuperDapp",
        description = "SuperDapp Implementation",
        url = "superdapp.com",
        icons = listOf("https://raw.githubusercontent.com/WalletConnect/walletconnect-assets/master/Icon/Gradient/Icon.png"),
        redirect = "superdapp://request"
    )
}