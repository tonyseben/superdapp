package com.example.superdapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.superdapp.ui.theme.SuperDappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SuperDappTheme {
                Scaffold(
                    topBar = {
                        //
                    },
                    bottomBar = {
                        //
                    }
                ) { innerPadding ->
                    // Apply the padding globally to the whole BottomNavScreensController
                    AppNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = Destination.Connect.route,
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destination.Connect.route) {
            ConnectScreen(navController, modifier)
        }
    }
}
