package com.example.superdapp.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.superdapp.domain.ConnectWalletUseCase
import com.example.superdapp.domain.SignMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(
    private val connectWalletUseCase: ConnectWalletUseCase,
    private val signMessageUseCase: SignMessageUseCase
) : ViewModel() {

    fun connectWallet(): Uri {
        return connectWalletUseCase()
    }

    fun signWallet(): Uri {
        return connectWalletUseCase()
    }

}