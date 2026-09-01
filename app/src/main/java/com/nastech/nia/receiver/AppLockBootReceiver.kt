package com.nastech.nia.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nastech.nia.data.local.prefs.AppLockPreferences
import com.nastech.nia.service.applock.AppLockService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppLockBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                val prefs = AppLockPreferences(context.applicationContext)
                runCatching {
                    if (prefs.settings.first().enabled) {
                        AppLockService.start(context.applicationContext)
                    }
                }
            }
        }
    }
}