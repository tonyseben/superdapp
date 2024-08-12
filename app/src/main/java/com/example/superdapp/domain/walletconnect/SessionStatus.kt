package com.example.superdapp.domain.walletconnect

import com.walletconnect.sign.client.Sign

sealed class SessionStatus {

    data class OnApproved(
        val approvedSession: Sign.Model.ApprovedSession
    ) : SessionStatus()

    data class OnSessionRequestResponse(
        val response: Sign.Model.SessionRequestResponse
    ) : SessionStatus()

    data class Error(val error: String) : SessionStatus()
}