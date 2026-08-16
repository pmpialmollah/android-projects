package com.nsoft.notificationhistory

import android.app.Application
import com.nsoft.notificationhistory.di.AppContainer

class MyApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
