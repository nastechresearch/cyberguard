package com.nastech.nia.ui.screens.vault

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nastech.nia.CyberGuardApp
import com.nastech.nia.core.security.VaultCrypto
import com.nastech.nia.data.repository.PinHash
import com.nastech.nia.data.local.prefs.VaultPreferences
import com.nastech.nia.data.repository.VaultEntry
import com.nastech.nia.data.repository.VaultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class VaultViewModel : ViewModel() {

    private val prefs = VaultPreferences(CyberGuardApp.context())
    private val repo = VaultRepository(CyberGuardApp.context(), prefs)

    private val _entries = mutableStateOf<List<VaultEntry>>(emptyList())
    val entries: State<List<VaultEntry>> = _entries

    private val _configured = mutableStateOf(false)
    val configured: State<Boolean> = _configured

    private val _justConfigured = mutableStateOf(false)
    val justConfigured: State<Boolean> = _justConfigured

    fun load() {
        viewModelScope.launch {
            _configured.value = prefs.settings.first().configured
            _entries.value = repo.listEntries()
        }
    }

    fun configure(pin: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = PinHash.create(pin)
            prefs.ensureConfigured()
            prefs.setPin(result.hash, result.salt, pin)
            _justConfigured.value = true
            _configured.value = true
        }
    }

    suspend fun unlock(pin: String): Boolean {
        val settings = prefs.settings.first()
        return PinHash.verify(pin, settings.pinHash, settings.pinSalt)
    }

    fun tryUnlock(onResult: (Boolean) -> Unit, pin: String) {
        viewModelScope.launch(Dispatchers.IO) { onResult(unlock(pin)) }
    }

    fun import(name: String, bytes: ByteArray) {
        viewModelScope.launch {
            repo.import(name, bytes)
            _entries.value = repo.listEntries()
        }
    }

    fun remove(name: String) {
        viewModelScope.launch {
            repo.delete(name)
            _entries.value = repo.listEntries()
        }
    }

    companion object {
        fun factory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = VaultViewModel() as T
            }
    }
}