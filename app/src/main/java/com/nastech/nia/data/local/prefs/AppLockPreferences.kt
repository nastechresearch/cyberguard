package com.nastech.nia.data.local.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.appLockDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "app_lock_prefs"
)

data class AppLockSettings(
    val enabled: Boolean = false,
    val hasPin: Boolean = false,
    val pinHash: String = "",
    val pinSalt: String = "",
    val timeoutMinutes: Int = 1,
    val lockedPackages: List<String> = emptyList()
)

class AppLockPreferences(private val context: Context) {

    private object Keys {
        val ENABLED = booleanPreferencesKey("enabled")
        val HAS_PIN = booleanPreferencesKey("has_pin")
        val PIN_HASH = stringPreferencesKey("pin_hash")
        val PIN_SALT = stringPreferencesKey("pin_salt")
        val TIMEOUT_MINUTES = intPreferencesKey("timeout_minutes")
        val LOCKED_PACKAGES = stringPreferencesKey("locked_packages")
    }

    val settings: Flow<AppLockSettings> = context.appLockDataStore.data.map { prefs ->
        AppLockSettings(
            enabled = prefs[Keys.ENABLED] ?: false,
            hasPin = prefs[Keys.HAS_PIN] ?: false,
            pinHash = prefs[Keys.PIN_HASH] ?: "",
            pinSalt = prefs[Keys.PIN_SALT] ?: "",
            timeoutMinutes = prefs[Keys.TIMEOUT_MINUTES] ?: 1,
            lockedPackages = decodeList(prefs[Keys.LOCKED_PACKAGES])
        )
    }

    val lockedPackages: Flow<Set<String>> = context.appLockDataStore.data.map { prefs ->
        decodeList(prefs[Keys.LOCKED_PACKAGES]).toSet()
    }

    suspend fun setEnabled(enabled: Boolean) {
        context.appLockDataStore.edit { it[Keys.ENABLED] = enabled }
    }

    suspend fun setPin(hash: String, salt: String) {
        context.appLockDataStore.edit {
            it[Keys.PIN_HASH] = hash
            it[Keys.PIN_SALT] = salt
            it[Keys.HAS_PIN] = true
        }
    }

    suspend fun clearPin() {
        context.appLockDataStore.edit {
            it.remove(Keys.PIN_HASH)
            it.remove(Keys.PIN_SALT)
            it[Keys.HAS_PIN] = false
        }
    }

    suspend fun setLockedPackages(packages: Set<String>) {
        context.appLockDataStore.edit { it[Keys.LOCKED_PACKAGES] = encodeList(packages) }
    }

    suspend fun setLocked(packageName: String, locked: Boolean) {
        val current = lockedPackages.first().toMutableSet()
        if (locked) current.add(packageName) else current.remove(packageName)
        context.appLockDataStore.edit { it[Keys.LOCKED_PACKAGES] = encodeList(current) }
    }

    suspend fun setTimeoutMinutes(minutes: Int) {
        context.appLockDataStore.edit { it[Keys.TIMEOUT_MINUTES] = minutes }
    }

    private fun encodeList(items: Set<String>): String = items.joinToString(",")

    private fun decodeList(raw: String?): List<String> =
        raw?.takeIf { it.isNotBlank() }?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
            ?: emptyList()
}