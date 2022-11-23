package com.luthfirr.musicplayeritune

import android.app.Application
import com.luthfirr.core.di.databaseModule
import com.luthfirr.core.di.networkModule
import com.luthfirr.core.di.repositoryModule
import com.luthfirr.musicplayeritune.di.useCaseModule
import com.luthfirr.musicplayeritune.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}