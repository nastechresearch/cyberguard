package com.nastech.nia.service.applock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastech.nia.data.local.prefs.AppLockPreferences
import com.nastech.nia.data.repository.PinHash
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LockViewModel : ViewModel() {

    var pin by mutableStateOf("")
        private set
    var hasError by mutableStateOf(false)
        private set
    var busy by mutableStateOf(false)
        private set

    fun append(digit: Char) {
        if (hasError) hasError = false
        if (pin.length < 6) pin += digit
    }

    fun delete() {
        if (pin.isNotEmpty()) pin = pin.dropLast(1)
    }

    fun clear() {
        pin = ""
        hasError = false
    }

    fun verify(pkg: String, onSuccess: () -> Unit) {
        if (pin.length < 6) {
            hasError = true
            return
        }
        busy = true
        viewModelScope.launch {
            val prefs = AppLockPreferences(AppLockAppContextHolder.get())
            val settings = prefs.settings.first()
            val ok = settings.hasPin &&
                PinHash.verify(pin, settings.pinHash, settings.pinSalt)
            if (ok) {
                com.nastech.nia.core.UnlockState.markUnlocked(
                    pkg,
                    settings.timeoutMinutes
                )
                onSuccess()
            } else {
                hasError = true
                pin = ""
            }
            busy = false
        }
    }
}

object AppLockAppContextHolder {
    @Volatile
    var context: android.content.Context? = null

    fun get(): android.content.Context =
        context ?: throw IllegalStateException("Context not initialized")
}