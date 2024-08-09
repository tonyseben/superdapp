package com.example.superdapp.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ConnectScreen(navController: NavController, modifier: Modifier = Modifier) {
    Text(
        text = "Super Dapp!",
        modifier = modifier
    )
}