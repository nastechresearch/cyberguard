package com.nastech.nia.data.repository

import android.content.Context
import android.content.Intent
import com.nastech.nia.core.UnlockState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Holds anti-theft protection state (armed/disarmed) and location logic. */
class AntiTheftRepository(private val context: Context) {

    private val _armed = MutableStateFlow(false)
    val armed: StateFlow<Boolean> = _armed.asStateFlow()

    private val _lastKnownLocation = MutableStateFlow<String?>(null)
    val lastKnownLocation: StateFlow<String?> = _lastKnownLocation.asStateFlow()

    fun setArmed(value: Boolean) {
        _armed.value = value
    }

    /** Simulated GPS fetch. Production: fused location provider. */
    suspend fun locate(): String {
        // Placeholder — read from settings source in real impl.
        val location = "Lat 3.375,-37.724 (approx)"
        _lastKnownLocation.value = location
        return location
    }

    suspend fun remoteLock() {
        UnlockState.clear()
    }
}