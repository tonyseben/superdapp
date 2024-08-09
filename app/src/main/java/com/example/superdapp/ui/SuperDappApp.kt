package com.example.superdapp.ui

import android.app.Application
import com.example.superdapp.BuildConfig
import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient
import com.walletconnect.android.relay.ConnectionType
import com.walletconnect.web3.modal.client.Modal
import com.walletconnect.web3.modal.client.Web3Modal
import com.walletconnect.web3.modal.presets.Web3ModalChainsPresets
import timber.log.Timber


class SuperDappApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initWalletConnect()
    }

    private fun initWalletConnect() {
        Timber.d("WalletConnect Initializing ...")
        val connectionType = ConnectionType.AUTOMATIC //or ConnectionType.MANUAL
        val projectId =
            "18c874cba7064d5a77f6787413a633bc" // Get Project ID at https://cloud.walletconnect.com/
        val appMetaData = Core.Model.AppMetaData(
            name = "Kotlin.Web3Modal",
            description = "Kotlin Web3Modal Implementation",
            url = "kotlin.walletconnect.com",
            icons = listOf("https://raw.githubusercontent.com/WalletConnect/walletconnect-assets/master/Icon/Gradient/Icon.png"),
            redirect = "kotlin-web3modal://request"
        )

        CoreClient.initialize(
            projectId = projectId,
            connectionType = connectionType,
            application = this,
            metaData = appMetaData
        ) {}

        Web3Modal.initialize(
            init = Modal.Params.Init(CoreClient),
            onSuccess = {
                // Callback will be called if initialization is successful
                Timber.d("WalletConnect INIT SUCCESS")
                Web3Modal.setChains(Web3ModalChainsPresets.ethChains.values.toList())
                Web3Modal.setDelegate(createDelegate())
                Web3Modal.setAuthRequestParams(createAuthParams())
            },
            onError = { error ->
                // Error will be thrown if there's an issue during initialization
                Timber.d("WalletConnect INIT FAILED: $error")
            }
        )
    }

    private fun createDelegate() = object : Web3Modal.ModalDelegate {
        override fun onSessionApproved(approvedSession: Modal.Model.ApprovedSession) {
            // Triggered when receives the session approval from wallet
            Timber.d("WalletConnect onSessionApproved")
        }

        override fun onSessionRejected(rejectedSession: Modal.Model.RejectedSession) {
            // Triggered when receives the session rejection from wallet
            Timber.d("WalletConnect onSessionRejected")
        }

        override fun onSessionUpdate(updatedSession: Modal.Model.UpdatedSession) {
            // Triggered when receives the session update from wallet
            Timber.d("WalletConnect onSessionUpdate")
        }

        override fun onSessionExtend(session: Modal.Model.Session) {
            // Triggered when receives the session extend from wallet
            Timber.d("WalletConnect onSessionExtend")
        }

        override fun onSessionEvent(sessionEvent: Modal.Model.SessionEvent) {
            // Triggered when the peer emits events that match the list of events agreed upon session settlement
            Timber.d("WalletConnect onSessionEvent")
        }

        override fun onSessionDelete(deletedSession: Modal.Model.DeletedSession) {
            // Triggered when receives the session delete from wallet
            Timber.d("WalletConnect onSessionDelete")
        }

        override fun onSessionRequestResponse(response: Modal.Model.SessionRequestResponse) {
            // Triggered when receives the session request response from wallet
            Timber.d("WalletConnect onSessionRequestResponse")
        }

        override fun onProposalExpired(proposal: Modal.Model.ExpiredProposal) {
            // Triggered when a proposal becomes expired
            Timber.d("WalletConnect onProposalExpired")
        }

        override fun onRequestExpired(request: Modal.Model.ExpiredRequest) {
            // Triggered when a request becomes expired
            Timber.d("WalletConnect onRequestExpired")
        }

        override fun onConnectionStateChange(state: Modal.Model.ConnectionState) {
            //Triggered whenever the connection state is changed
            Timber.d("WalletConnect onConnectionStateChange $state")
        }

        override fun onError(error: Modal.Model.Error) {
            // Triggered whenever there is an issue inside the SDK
            Timber.d("WalletConnect onError")
        }


        override fun onSessionAuthenticateResponse(response: Modal.Model.SessionAuthenticateResponse) {
            // Triggered when Dapp receives the session authenticate response from wallet

            if (response is Modal.Model.SessionAuthenticateResponse.Result) {
                if (response.session != null) {
                    // Authentication successful, session established
                    Timber.d("WalletConnect onSessionAuthenticateResponse: Authentication successful, session established")
                } else {
                    // Authentication successful, but no session created (SIWE-only flow)
                    Timber.d("WalletConnect onSessionAuthenticateResponse: Authentication successful, but no session created (SIWE-only flow)")
                }
            } else {
                // Authentication request was rejected or failed
                Timber.d("WalletConnect onSessionAuthenticateResponse: Authentication request was rejected or failed")
            }
        }

        override fun onSIWEAuthenticationResponse(response: Modal.Model.SIWEAuthenticateResponse) {
            if (response is Modal.Model.SIWEAuthenticateResponse.Result) {
                // message and signature
                Timber.d("WalletConnect onSIWEAuthenticationResponse: $response")
            } else {
                //error
                Timber.d("WalletConnect onSIWEAuthenticationResponse: Error $response")
            }
        }
    }

    private fun createAuthParams() = Modal.Model.AuthPayloadParams(
        chains = listOf("eip155:1", "eip155:137"),
        domain = "yourDappDomain.com",
        uri = "https://yourDappDomain.com/login",
        nonce = "5",
        statement = "I accept the Terms of Service: https://yourDappDomain.com/",
        methods = listOf("personal_sign", "eth_sendTransaction"),
        resources = null // Here your dapp may request authorization with ReCaps
    )
}