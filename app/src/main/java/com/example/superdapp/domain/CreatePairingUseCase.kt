package com.example.superdapp.domain

import android.net.Uri
import androidx.core.net.toUri
import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient

class CreatePairingUseCase {

    operator fun invoke(): Core.Model.Pairing {
        return CoreClient.Pairing.create()
            ?: throw Exception("Pairing creation failed!")
    }

    fun uri(): Uri {
        return invoke().uri.toUri()
    }

}