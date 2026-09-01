package com.nastech.nia.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import com.nastech.nia.data.local.prefs.AppLockPreferences
import com.nastech.nia.data.model.InstalledApp
import com.nastech.nia.core.UnlockState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AppLockRepository(
    private val context: Context,
    private val prefs: AppLockPreferences
) {

    val settings: Flow<AppLockSettingsState> = prefs.settings.map { s ->
        AppLockSettingsState(
            enabled = s.enabled,
            hasPin = s.hasPin,
            timeoutMinutes = s.timeoutMinutes
        )
    }

    val lockedPackages: Flow<Set<String>> = prefs.lockedPackages

    suspend fun setEnabled(enabled: Boolean) {
        prefs.setEnabled(enabled)
        if (!enabled) clearUnlockState()
    }

    suspend fun savePin(pin: String) {
        val (hash, salt) = PinHash.create(pin)
        prefs.setPin(hash, salt)
    }

    suspend fun verifyPin(pin: String): Boolean {
        val settings = prefs.settings.first()
        if (!settings.hasPin) return false
        return PinHash.verify(pin, settings.pinHash, settings.pinSalt)
    }

    suspend fun hasPin(): Boolean = prefs.settings.first().hasPin

    suspend fun setLocked(packageName: String, locked: Boolean) {
        prefs.setLocked(packageName, locked)
    }

    suspend fun changeTimeout(minutes: Int) {
        prefs.setTimeoutMinutes(minutes)
    }

    suspend fun getInstalledApps(): List<InstalledApp> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val intent = android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
            addCategory(android.content.Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfos: List<ResolveInfo> = if (android.os.Build.VERSION.SDK_INT >= 33) {
            pm.queryIntentActivities(
                intent,
                android.content.pm.PackageManager.ResolveInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            pm.queryIntentActivities(intent, 0)
        }
        resolveInfos
            .asSequence()
            .map { ri ->
                val pkg = ri.activityInfo?.packageName ?: return@map null
                if (pkg == context.packageName) return@map null
                val appName = ri.loadLabel(pm).toString()
                val icon = ri.loadIcon(pm)
                InstalledApp(packageName = pkg, appName = appName, icon = icon)
            }
            .filterNotNull()
            .distinctBy { it.packageName }
            .sortedBy { it.appName.lowercase() }
            .toList()
    }

    fun loadIcon(packageName: String): Drawable? = try {
        context.packageManager.getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }

    suspend fun isUnlocked(packageName: String): Boolean = UnlockState.isUnlocked(packageName)

    suspend fun markUnlocked(packageName: String) {
        UnlockState.markUnlocked(packageName, prefs.settings.first().timeoutMinutes)
    }

    suspend fun clearUnlockState() {
        UnlockState.clear()
    }
}

data class AppLockSettingsState(
    val enabled: Boolean = false,
    val hasPin: Boolean = false,
    val timeoutMinutes: Int = 1
)