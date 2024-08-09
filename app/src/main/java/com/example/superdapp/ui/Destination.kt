package com.example.superdapp.ui

sealed class Destination(val route: String) {
    data object Connect : Destination("Connect")
}