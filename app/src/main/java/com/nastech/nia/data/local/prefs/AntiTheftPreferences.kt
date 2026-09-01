package com.nastech.nia.data.local.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.antiTheftDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "antitheft_prefs"
)

data class AntiTheftSettings(
    val armed: Boolean = false,
    val simAlertEnabled: Boolean = false,
    val registeredSimSerial: String = "",
    val smsCommandsEnabled: Boolean = false,
    val emergencySmsEnabled: Boolean = false,
    val currentGps: String = "No signal"
)

class AntiTheftPreferences(private val context: Context) {

    private object Keys {
        val ARMED = booleanPreferencesKey("armed")
        val SIM_ALERT = booleanPreferencesKey("sim_alert")
        val SIM_SERIAL = stringPreferencesKey("sim_serial")
        val SMS_COMMANDS = booleanPreferencesKey("sms_commands")
        val EMERGENCY_SMS = booleanPreferencesKey("emergency_sms")
        val CURRENT_GPS = stringPreferencesKey("current_gps")
    }

    val settings: Flow<AntiTheftSettings> = context.antiTheftDataStore.data.map { prefs ->
        AntiTheftSettings(
            armed = prefs[Keys.ARMED] ?: false,
            simAlertEnabled = prefs[Keys.SIM_ALERT] ?: false,
            registeredSimSerial = prefs[Keys.SIM_SERIAL] ?: "",
            smsCommandsEnabled = prefs[Keys.SMS_COMMANDS] ?: false,
            emergencySmsEnabled = prefs[Keys.EMERGENCY_SMS] ?: false,
            currentGps = prefs[Keys.CURRENT_GPS] ?: "No signal"
        )
    }

    suspend fun setArmed(armed: Boolean) {
        context.antiTheftDataStore.edit { it[Keys.ARMED] = armed }
    }

    suspend fun setSimAlertEnabled(enabled: Boolean) {
        context.antiTheftDataStore.edit { it[Keys.SIM_ALERT] = enabled }
    }

    suspend fun setRegisteredSimSerial(serial: String) {
        context.antiTheftDataStore.edit { it[Keys.SIM_SERIAL] = serial }
    }

    suspend fun setSmsCommandsEnabled(enabled: Boolean) {
        context.antiTheftDataStore.edit { it[Keys.SMS_COMMANDS] = enabled }
    }

    suspend fun setEmergencySmsEnabled(enabled: Boolean) {
        context.antiTheftDataStore.edit { it[Keys.EMERGENCY_SMS] = enabled }
    }

    suspend fun updateGps(text: String) {
        context.antiTheftDataStore.edit { it[Keys.CURRENT_GPS] = text }
    }
}