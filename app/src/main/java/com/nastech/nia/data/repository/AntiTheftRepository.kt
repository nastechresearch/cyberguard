package com.nastech.nia.data.repository

import android.content.Context
import android.telephony.TelephonyManager
import com.nastech.nia.core.UnlockState
import com.nastech.nia.core.security.SmsCommandParser
import com.nastech.nia.data.local.prefs.AntiTheftPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

/** Holds anti-theft protection state (armed/disarmed), SIM tracking and SMS commands. */
class AntiTheftRepository(private val context: Context) {

    private val prefs = AntiTheftPreferences(context)

    val armed: Flow<Boolean> = prefs.settings.map { it.armed }
    val simAlertEnabled: Flow<Boolean> = prefs.settings.map { it.simAlertEnabled }
    val smsCommandsEnabled: Flow<Boolean> = prefs.settings.map { it.smsCommandsEnabled }
    val lastKnownLocation: Flow<String> = prefs.settings.map { it.currentGps }

    suspend fun setArmed(value: Boolean) = prefs.setArmed(value)

    suspend fun setSimAlertEnabled(enabled: Boolean) = prefs.setSimAlertEnabled(enabled)

    suspend fun setSmsCommandsEnabled(enabled: Boolean) = prefs.setSmsCommandsEnabled(enabled)

    /** Reads the current SIM serial number (requires READ_PHONE_STATE). */
    @android.annotation.SuppressLint("MissingPermission")
    fun readCurrentSimSerial(): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        return try {
            tm?.simSerialNumber ?: "SIM_${System.currentTimeMillis()}"
        } catch (e: SecurityException) {
            "permission_denied"
        }
    }

    /** Registers the current SIM as trusted so future SIM changes are flagged. */
    suspend fun registerCurrentSim() {
        val serial = readCurrentSimSerial()
        if (serial.isNotBlank()) {
            prefs.setRegisteredSimSerial(serial)
            prefs.setSimAlertEnabled(true)
        }
    }

    /** True if the currently inserted SIM differs from the registered one. */
    suspend fun isSimChanged(): Boolean {
        val settings = prefs.settings.first()
        if (!settings.simAlertEnabled || settings.registeredSimSerial.isBlank()) return false
        val current = readCurrentSimSerial()
        return current != "permission_denied" && current != settings.registeredSimSerial
    }

    /** Processes an incoming SMS body against the armed anti-theft command set. */
    suspend fun handleIncomingSms(body: String?): SmsCommandParser.Command {
        if (prefs.settings.first().smsCommandsEnabled.not()) {
            return SmsCommandParser.Command.Unrecognized
        }
        val command = SmsCommandParser.parse(body)
        when (command) {
            SmsCommandParser.Command.Locate -> {
                prefs.updateGps(locate())
            }
            SmsCommandParser.Command.Lock -> {
                prefs.setArmed(true)
                UnlockState.clear()
            }
            SmsCommandParser.Command.Wipe -> {
                prefs.setArmed(true)
            }
            SmsCommandParser.Command.Alarm -> {
                // Trigger audible alarm in production.
            }
            else -> Unit
        }
        return command
    }

    /** Simulated GPS fetch. Production: fused location provider. */
    suspend fun locate(): String {
        val location = "Lat 3.375,-37.724 (approx)"
        prefs.updateGps(location)
        return location
    }

    suspend fun remoteLock() {
        prefs.setArmed(true)
        UnlockState.clear()
    }
}