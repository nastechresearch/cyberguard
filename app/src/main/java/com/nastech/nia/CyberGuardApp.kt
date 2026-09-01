package com.nastech.nia

import android.app.Application
import android.content.Context

/** Application-scoped access to the app context for lightweight manual DI. */
class CyberGuardApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        @Volatile
        lateinit var instance: CyberGuardApp
            private set

        fun context(): Context = instance.applicationContext
    }
}