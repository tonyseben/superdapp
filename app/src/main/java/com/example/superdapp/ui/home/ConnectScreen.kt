package com.example.superdapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.superdapp.R
import com.walletconnect.web3.modal.client.Web3Modal
import com.walletconnect.web3.modal.ui.components.button.AccountButton
import com.walletconnect.web3.modal.ui.components.button.AccountButtonType
import com.walletconnect.web3.modal.ui.components.button.ConnectButton
import com.walletconnect.web3.modal.ui.components.button.ConnectButtonSize
import com.walletconnect.web3.modal.ui.components.button.NetworkButton
import com.walletconnect.web3.modal.ui.components.button.rememberWeb3ModalState
import timber.log.Timber

@Composable
fun ConnectScreen(navController: NavController, modifier: Modifier = Modifier) {

    val web3ModalState = rememberWeb3ModalState(navController = navController)
    val address = Web3Modal.getAccount()?.address
    val isConnected by web3ModalState.isConnected.collectAsState()

    Timber.d("address: $address")
    Timber.d("isConnected: $isConnected")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(R.mipmap.ic_launcher_foreground),
            contentDescription = "content description",
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(40.dp))

        if (!isConnected) {
            ConnectButton(state = web3ModalState, buttonSize = ConnectButtonSize.NORMAL)
        } else {
            NetworkButton(state = web3ModalState)
            Spacer(modifier = Modifier.height(10.dp))
            AccountButton(web3ModalState, AccountButtonType.NORMAL)
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = {
                Web3Modal.disconnect(
                    onSuccess = {},
                    onError = { error: Throwable -> }
                )
            }) {
                Text(text = "Disconnect")
            }
        }
    }
}