package com.example.superdapp.domain

import com.walletconnect.sign.client.Sign
import com.walletconnect.sign.client.SignClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class SignClientDelegate @Inject constructor() {

    operator fun invoke(): Flow<SessionStatus> = callbackFlow {

        val dappDelegate = object : SignClient.DappDelegate {

            override fun onSessionApproved(approvedSession: Sign.Model.ApprovedSession) {
                // Triggered when Dapp receives the session approval from wallet
                Timber.d("WalletConnect onSessionApproved $approvedSession")
                trySend(SessionStatus.OnApproved(approvedSession))
            }

            override fun onSessionRejected(rejectedSession: Sign.Model.RejectedSession) {
                // Triggered when Dapp receives the session rejection from wallet
                Timber.d("WalletConnect onSessionRejected")
            }

            override fun onSessionUpdate(updatedSession: Sign.Model.UpdatedSession) {
                // Triggered when Dapp receives the session update from wallet
                Timber.d("WalletConnect onSessionUpdate")
            }

            override fun onSessionExtend(session: Sign.Model.Session) {
                // Triggered when Dapp receives the session extend from wallet
                Timber.d("WalletConnect onSessionExtend")
            }

            override fun onSessionEvent(sessionEvent: Sign.Model.SessionEvent) {
                // Triggered when the peer emits events that match the list of events agreed upon session settlement
                Timber.d("WalletConnect onSessionEvent")
            }

            override fun onSessionDelete(deletedSession: Sign.Model.DeletedSession) {
                // Triggered when Dapp receives the session delete from wallet
                Timber.d("WalletConnect onSessionDelete $deletedSession")
            }

            override fun onSessionRequestResponse(response: Sign.Model.SessionRequestResponse) {
                // Triggered when Dapp receives the session request response from wallet
                Timber.d("WalletConnect onSessionRequestResponse $response")
                trySend(SessionStatus.OnSessionRequestResponse(response))
            }

            override fun onProposalExpired(proposal: Sign.Model.ExpiredProposal) {
                // Triggered when a proposal becomes expired
                Timber.d("WalletConnect onProposalExpired")
            }

            override fun onRequestExpired(request: Sign.Model.ExpiredRequest) {
                // Triggered when a request becomes expired
                Timber.d("WalletConnect onRequestExpired")
            }

            override fun onConnectionStateChange(state: Sign.Model.ConnectionState) {
                //Triggered whenever the connection state is changed
                Timber.d("WalletConnect onConnectionStateChange $state")
            }

            override fun onError(error: Sign.Model.Error) {
                // Triggered whenever there is an issue inside the SDK
                Timber.d("WalletConnect onError")
                trySend(SessionStatus.Error(error.toString()))
            }


            override fun onSessionAuthenticateResponse(response: Sign.Model.SessionAuthenticateResponse) {
                // Triggered when Dapp receives the session authenticate response from wallet

                if (response is Sign.Model.SessionAuthenticateResponse.Result) {
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
        }

        SignClient.setDappDelegate(dappDelegate)
        awaitClose()
    }
}