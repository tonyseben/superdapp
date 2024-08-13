package com.example.superdapp.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.superdapp.R
import com.example.superdapp.domain.walletconnect.ConnectStatus
import com.example.superdapp.domain.walletconnect.DisconnectStatus
import com.example.superdapp.domain.walletconnect.SignStatus
import com.example.superdapp.ui.utils.NetworkUtils
import com.lightspark.composeqr.QrCodeView

@Composable
fun ConnectScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ConnectViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    val (tabSelected, setTabSelected) = remember {
        mutableIntStateOf(0)
    }

    val isConnected by NetworkUtils.observeConnection(context).collectAsState(true)
    if (!isConnected) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(Color(0xFFFF4848)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Not connected to internet!",
                color = Color.White
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 8.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(R.mipmap.ic_launcher_foreground),
            contentDescription = "content description",
            modifier = Modifier.size(170.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (state.signStatus !is SignStatus.Signed) {

            CustomButton(
                text = when (state.connectStatus) {
                    is ConnectStatus.Default -> "Connect Wallet"
                    is ConnectStatus.ConnectRequested -> "Awaiting wallet"
                    ConnectStatus.Connecting -> "Connecting ..."
                    is ConnectStatus.Connected -> "Connected"
                    is ConnectStatus.Error -> "Connect Wallet"
                },
                enabled = when (state.connectStatus) {
                    is ConnectStatus.Default,
                    is ConnectStatus.Error -> true

                    else -> false
                },
                onClick = { viewModel.setEvent(ConnectContract.Event.OnConnectClick) })

            Spacer(modifier = Modifier.height(10.dp))

            if (state.connectStatus is ConnectStatus.ConnectRequested) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.White, RoundedCornerShape(4.dp)),

                    ) {
                    CustomTab(
                        items = listOf("Via QRCode", "Via Wallet"),
                        selectedItemIndex = tabSelected,
                        onClick = setTabSelected,
                    )

                    when (tabSelected) {
                        0 -> {
                            state.pairingUrl?.let {
                                Spacer(modifier = Modifier.height(10.dp))
                                QrCodeView(
                                    data = it,
                                    modifier = Modifier.size(250.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }

                        1 -> {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(state.pairingUrl))
                            context.startActivity(intent)
                        }
                    }
                }
            } else if (state.connectStatus is ConnectStatus.Connected) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.White, RoundedCornerShape(4.dp)),

                    ) {
                    CustomButton(
                        text = when (state.signStatus) {
                            SignStatus.Default -> "Sign Message"
                            SignStatus.Signing -> "Signing ..."
                            SignStatus.SignRequested -> "Awaiting wallet"
                            SignStatus.Signed -> "Signed"
                            is SignStatus.Error -> "Sign Message"
                        },
                        enabled = when (state.signStatus) {
                            is SignStatus.Default,
                            is SignStatus.Error -> true

                            else -> false
                        },
                        onClick = { viewModel.setEvent(ConnectContract.Event.OnSignClick) })

                    if (state.signStatus is SignStatus.SignRequested && tabSelected == 1) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(state.pairingUrl))
                        context.startActivity(intent)
                    }
                }
            }

            if (state.connectStatus !is ConnectStatus.Default) {
                Spacer(modifier = Modifier.height(10.dp))
                IconButton(
                    onClick = { viewModel.setEvent(ConnectContract.Event.OnCancelClick) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cancel),
                        contentDescription = "Cancel",
                        tint = Color.Blue,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

        } else {
            Text(
                text = "Connected to ${state.connectedWallet}, Signed",
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomButton(
                text = when (state.disConnectStatus) {
                    DisconnectStatus.Default -> "Disconnect Wallet"
                    DisconnectStatus.Disconnected -> "Disconnected"
                    is DisconnectStatus.Error -> "Disconnect Wallet"
                },
                enabled = when (state.disConnectStatus) {
                    is DisconnectStatus.Default,
                    is DisconnectStatus.Error -> true

                    else -> false
                },
                onClick = { viewModel.setEvent(ConnectContract.Event.OnDisconnectClick) })

            Spacer(modifier = Modifier.height(20.dp))

            var accounts = "Account(s):\n"
            for (acc in state.accounts) {
                accounts += "\n\u2022 $acc"
            }
            Text(
                text = accounts,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}
