package com.example.superdapp.domain

import com.walletconnect.android.Core
import javax.inject.Inject

class GetAppMetadataUseCase @Inject constructor() {

    operator fun invoke() = Core.Model.AppMetaData(
        name = "SuperDapp",
        description = "SuperDapp Implementation",
        url = "superdapp.com",
        icons = listOf("https://raw.githubusercontent.com/WalletConnect/walletconnect-assets/master/Icon/Gradient/Icon.png"),
        redirect = "superdapp://request"
    )
}