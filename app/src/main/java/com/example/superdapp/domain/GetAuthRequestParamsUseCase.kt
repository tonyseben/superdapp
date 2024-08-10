package com.example.superdapp.domain

import com.walletconnect.web3.modal.client.Modal
import javax.inject.Inject

class GetAuthRequestParamsUseCase @Inject constructor() {

    operator fun invoke() = Modal.Model.AuthPayloadParams(
        chains = listOf("eip155:1", "eip155:137"),
        domain = "superdapp.com",
        uri = "https://yourDappDomain.com/login",
        nonce = "7",
        statement = "I accept the Terms of Service: https://superdapp.com/",
        methods = listOf("personal_sign"/*, "eth_sendTransaction"*/),
        resources = null // Here your dapp may request authorization with ReCaps
    )
}