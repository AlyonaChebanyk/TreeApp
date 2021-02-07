package com.example.treeapp

import android.app.Application
import com.example.treeapp.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class TreeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TreeApp)
            modules(
                    listOf(
                            networkModule
                    )
            )
        }

        Timber.plant(Timber.DebugTree())
    }

}