package com.example.testwidget

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class WidgetApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@WidgetApp)
            modules(com.example.testwidget.di.modules)
        }
    }
}
