package com.example.superdapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.walletconnect.web3.modal.client.Web3Modal
import com.walletconnect.web3.modal.ui.components.button.AccountButton
import com.walletconnect.web3.modal.ui.components.button.AccountButtonType
import com.walletconnect.web3.modal.ui.components.button.ConnectButton
import com.walletconnect.web3.modal.ui.components.button.ConnectButtonSize
import com.walletconnect.web3.modal.ui.components.button.NetworkButton
import com.walletconnect.web3.modal.ui.components.button.rememberWeb3ModalState

@Composable
fun ConnectScreen(navController: NavController, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
    ) {
        val web3ModalState = rememberWeb3ModalState(navController = navController)
        val address = Web3Modal.getAccount()?.address

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            ConnectButton(state = web3ModalState, buttonSize = ConnectButtonSize.NORMAL)
            Spacer(modifier = Modifier.height(40.dp))

            NetworkButton(state = web3ModalState)
            Spacer(modifier = Modifier.height(10.dp))
            AccountButton(web3ModalState, AccountButtonType.NORMAL)
        }
    }

}