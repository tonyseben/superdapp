package com.example.superdapp.domain.walletconnect

import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient
import javax.inject.Inject

class CreatePairingUseCase @Inject constructor() {

    operator fun invoke(): Core.Model.Pairing {
        return CoreClient.Pairing.create()
            ?: throw Exception("Pairing creation failed!")
    }
}