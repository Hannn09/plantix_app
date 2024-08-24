package com.example.plantix

import android.app.Application
import com.example.core.di.networkModule
import com.example.core.di.repositoryModule
import com.example.plantix.di.useCaseModule
import com.example.plantix.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@BaseApplication)
            modules(listOf(
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            ))
        }
    }
}