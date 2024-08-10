package com.example.superdapp.ui

import android.app.Application
import com.example.superdapp.BuildConfig
import com.example.superdapp.ui.auth.WalletAuth
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SuperDappApp : Application() {

    @Inject
    lateinit var walletAuth: WalletAuth

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        walletAuth.init()
    }
}