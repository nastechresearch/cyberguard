package com.nastech.nia.data.local.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nastech.nia.core.security.VaultCrypto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Base64
import javax.crypto.SecretKey

private val Context.vaultDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "vault_prefs"
)

data class VaultSettings(
    val configured: Boolean = false,
    val hasPin: Boolean = false,
    val pinHash: String = "",
    val pinSalt: String = "",
    val rawPin: String = ""
)

class VaultPreferences(private val context: Context) {

    private object Keys {
        val CONFIGURED = booleanPreferencesKey("configured")
        val HAS_PIN = booleanPreferencesKey("has_pin")
        val PIN_HASH = stringPreferencesKey("pin_hash")
        val PIN_SALT = stringPreferencesKey("pin_salt")
        val RAW_PIN = stringPreferencesKey("raw_pin")
        val SECRET_KEY = stringPreferencesKey("secret_key")
    }

    val settings: Flow<VaultSettings> = context.vaultDataStore.data.map { prefs ->
        VaultSettings(
            configured = prefs[Keys.CONFIGURED] ?: false,
            hasPin = prefs[Keys.HAS_PIN] ?: false,
            pinHash = prefs[Keys.PIN_HASH] ?: "",
            pinSalt = prefs[Keys.PIN_SALT] ?: "",
            rawPin = prefs[Keys.RAW_PIN] ?: ""
        )
    }

    suspend fun ensureConfigured(): SecretKey {
        val existing = context.vaultDataStore.data.map { it[Keys.SECRET_KEY] }.first()
        if (existing != null) return decodeKey(existing)
        val key = VaultCrypto.generateKey()
        context.vaultDataStore.edit { it[Keys.SECRET_KEY] = encodeKey(key) }
        return key
    }

    suspend fun getSecretKey(): SecretKey? {
        val raw = context.vaultDataStore.data.map { it[Keys.SECRET_KEY] }.first()
        return raw?.let { decodeKey(it) }
    }

    suspend fun setPin(hash: String, salt: String, rawPin: String) {
        context.vaultDataStore.edit {
            it[Keys.HAS_PIN] = true
            it[Keys.PIN_HASH] = hash
            it[Keys.PIN_SALT] = salt
            it[Keys.RAW_PIN] = rawPin
            it[Keys.CONFIGURED] = true
        }
    }

    suspend fun removeRawPin() {
        context.vaultDataStore.edit { it.remove(Keys.RAW_PIN) }
    }

    private fun encodeKey(key: SecretKey): String =
        Base64.getEncoder().encodeToString(key.encoded)

    private fun decodeKey(encoded: String): SecretKey {
        val spec = javax.crypto.spec.SecretKeySpec(
            Base64.getDecoder().decode(encoded),
            "AES"
        )
        return spec
    }
}