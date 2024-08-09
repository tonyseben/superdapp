package com.example.superdapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.walletconnect.web3.modal.ui.components.button.ConnectButton
import com.walletconnect.web3.modal.ui.components.button.ConnectButtonSize
import com.walletconnect.web3.modal.ui.components.button.rememberWeb3ModalState

@Composable
fun ConnectScreen(navController: NavController, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Super Dapp!",
            modifier = modifier
        )

        val web3ModalState = rememberWeb3ModalState(navController = navController)
        ConnectButton(
            state = web3ModalState,
            buttonSize = ConnectButtonSize.NORMAL
        )
    }

}