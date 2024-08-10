package com.example.superdapp.ui.auth

import android.app.Application
import com.example.superdapp.domain.GetAppMetadataUseCase
import com.example.superdapp.domain.Web3ModalDelegate
import com.walletconnect.android.CoreClient
import com.walletconnect.android.relay.ConnectionType
import timber.log.Timber
import javax.inject.Inject

class WalletConnectAuth @Inject constructor(
    private val application: Application,
    private val getAppMetadata: GetAppMetadataUseCase,
    private val web3ModalDelegate: Web3ModalDelegate
) : WalletAuth {
    override fun init() {
        val projectId = "18c874cba7064d5a77f6787413a633bc" // https://cloud.walletconnect.com/

        CoreClient.initialize(
            projectId = projectId,
            connectionType = ConnectionType.AUTOMATIC,
            application = application,
            metaData = getAppMetadata()
        ) { error ->
            Timber.d("WalletConnect CoreClient INIT FAILED: $error")
        }

        web3ModalDelegate.init()
    }
}