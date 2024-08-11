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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

@Composable
fun ConnectScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ConnectViewModel = hiltViewModel()
) {
    val context = LocalContext.current

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
        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = {
            viewModel.connectWallet()?.let { uri ->
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
        }) {
            Text(text = "Connect Wallet")
        }
    }
}