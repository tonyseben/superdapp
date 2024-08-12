package com.example.superdapp.domain

import android.net.Uri
import androidx.core.net.toUri
import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient
import javax.inject.Inject

class CreatePairingUseCase @Inject constructor() {

    operator fun invoke(): Core.Model.Pairing {
        return CoreClient.Pairing.create()
            ?: throw Exception("Pairing creation failed!")
    }

    fun getUri(pairing: Core.Model.Pairing): Uri {
        return pairing.uri.toUri()
    }

}