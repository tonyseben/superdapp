package com.example.superdapp.ui.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.superdapp.domain.ConnectStatus
import com.example.superdapp.domain.DisconnectStatus
import com.example.superdapp.domain.SignStatus
import timber.log.Timber

@Composable
fun ConnectScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ConnectViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    if (state.connectStatus is ConnectStatus.ConnectRequested
        || state.signStatus is SignStatus.SignRequested
    ) {
        Timber.d("URI: ${state.pairingUri}")
        val intent = Intent(Intent.ACTION_VIEW, state.pairingUri)
        context.startActivity(intent)
    }

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 8.dp)
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
        Spacer(modifier = Modifier.height(20.dp))

        if (state.signStatus !is SignStatus.Signed) {
            Button(
                onClick = {
                    viewModel.setEvent(ConnectContract.Event.OnConnectClick)
                },
                enabled = when (state.connectStatus) {
                    is ConnectStatus.Default,
                    is ConnectStatus.Error -> true

                    else -> false
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Blue,
                    contentColor = Color.White,
                    disabledBackgroundColor = contentColor.copy(alpha = ContentAlpha.disabled),
                    disabledContentColor = contentColor.copy(alpha = ContentAlpha.disabled),
                ),
                modifier = Modifier.size(width = 200.dp, height = 42.dp)
            ) {
                Text(
                    text = when (state.connectStatus) {
                        is ConnectStatus.Default -> "Connect Wallet"
                        is ConnectStatus.ConnectRequested -> "Awaiting wallet"
                        ConnectStatus.Connecting -> "Connecting ..."
                        is ConnectStatus.Connected -> "Connected"
                        is ConnectStatus.Error -> "Connect Wallet"
                    },
                    fontSize = 16.sp
                )
            }

            if (state.connectStatus is ConnectStatus.Connected) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        viewModel.setEvent(ConnectContract.Event.OnSignClick)
                    },
                    enabled = when (state.signStatus) {
                        is SignStatus.Default,
                        is SignStatus.Error -> true

                        else -> false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Blue,
                        contentColor = Color.White,
                        disabledBackgroundColor = contentColor.copy(alpha = ContentAlpha.disabled),
                        disabledContentColor = contentColor.copy(alpha = ContentAlpha.disabled),
                    ),
                    modifier = Modifier.size(width = 200.dp, height = 42.dp)
                ) {
                    Text(
                        text = when (state.signStatus) {
                            SignStatus.Default -> "Sign Message"
                            SignStatus.Signing -> "Signing ..."
                            SignStatus.SignRequested -> "Awaiting wallet"
                            SignStatus.Signed -> "Signed"
                            is SignStatus.Error -> "Sign Message"
                        },
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            Text(
                text = "Connected to ${state.connectedWallet}, Signed",
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    viewModel.setEvent(ConnectContract.Event.OnDisconnectClick)
                },
                enabled = when (state.disConnectStatus) {
                    is DisconnectStatus.Default,
                    is DisconnectStatus.Error -> true

                    else -> false
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Blue,
                    contentColor = Color.White,
                    disabledBackgroundColor = contentColor.copy(alpha = ContentAlpha.disabled),
                    disabledContentColor = contentColor.copy(alpha = ContentAlpha.disabled),
                ),
                modifier = Modifier.size(width = 200.dp, height = 42.dp)
            ) {
                Text(
                    text = when (state.disConnectStatus) {
                        DisconnectStatus.Default -> "Disconnect Wallet"
                        DisconnectStatus.Disconnected -> "Disconnected"
                        is DisconnectStatus.Error -> "Disconnect Wallet"
                    },
                    fontSize = 16.sp
                )
            }
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
