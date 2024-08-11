package com.example.superdapp.domain

import com.walletconnect.sign.client.Sign

sealed class SessionStatus {

    data class OnApproved(val approvedSession: Sign.Model.ApprovedSession) : SessionStatus()
    data class Error(val error: String) : SessionStatus()
}