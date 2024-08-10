package com.example.superdapp.di

import com.example.superdapp.ui.auth.WalletAuth
import com.example.superdapp.ui.auth.WalletConnectAuth
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt {

        @Binds
        fun bindWalletAuth(walletAuth: WalletConnectAuth): WalletAuth
    }
}