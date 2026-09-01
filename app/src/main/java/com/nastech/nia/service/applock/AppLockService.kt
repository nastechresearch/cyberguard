package com.nastech.nia.service.applock

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import com.nastech.nia.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppLockService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private var pollJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val running = NotificationUtil.buildNotification(this)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                running,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, running)
        }
        startPolling()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        NotificationUtil.updateNotification(this)
        return START_STICKY
    }

    private fun startPolling() {
        pollJob?.cancel()
        pollJob = scope.launch {
            val prefsPreferences = AppLockPreferencesHolder.get(this@AppLockService)
            var lastForegroundPackage: String? = null
            while (true) {
                runCatching {
                    val settings = prefsPreferences.settings.first()
                    if (!settings.enabled) {
                        stopSelf()
                        return@launch
                    }
                    val lockedSet = prefsPreferences.lockedPackages.first()
                    val foreground = ForegroundAppDetector.getForegroundPackage(this@AppLockService)
                    if (foreground != null && foreground != lastForegroundPackage) {
                        lastForegroundPackage = foreground
                        if (foreground in lockedSet &&
                            !UnlockStateBridge.isUnlocked(foreground)
                        ) {
                            launchLockActivity(foreground)
                        }
                    }
                }
                delay(POLL_INTERVAL_MS)
            }
        }
    }

    private fun launchLockActivity(packageName: String) {
        val intent = Intent(this, LockActivity::class.java).apply {
            putExtra(LockActivity.EXTRA_PACKAGE, packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.applock_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.applock_channel_desc)
                setShowBadge(false)
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        pollJob?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "app_lock"
        const val NOTIFICATION_ID = 4001
        private const val POLL_INTERVAL_MS = 400L

        fun start(context: Context) {
            val intent = Intent(context, AppLockService::class.java)
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, AppLockService::class.java))
        }
    }
}

/** Bridges UnlockState so the service can check without repository coupling. */
object UnlockStateBridge {
    suspend fun isUnlocked(packageName: String): Boolean =
        com.nastech.nia.core.UnlockState.isUnlocked(packageName)
}

object AppLockPreferencesHolder {
    fun get(context: Context): com.nastech.nia.data.local.prefs.AppLockPreferences =
        com.nastech.nia.data.local.prefs.AppLockPreferences(context)
}