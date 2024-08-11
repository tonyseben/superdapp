package com.example.superdapp.ui.main

sealed class Destination(val route: String) {
    data object Connect : Destination("Connect")
    data object Home : Destination("Home")
}