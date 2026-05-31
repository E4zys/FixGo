package com.example.fixgo

import android.app.Application
import com.example.core.di.networkModule
import com.example.fixgo.di.appModule
import com.example.user.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FixGoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FixGoApplication)
            modules(
                networkModule,
                userModule,
                appModule
            )
        }
    }
}
