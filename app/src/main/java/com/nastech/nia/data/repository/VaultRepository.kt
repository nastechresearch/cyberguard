package com.nastech.nia.data.repository

import android.content.Context
import com.nastech.nia.core.security.VaultCrypto
import com.nastech.nia.data.local.prefs.VaultPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class VaultEntry(val name: String, val sizeBytes: Long)

/** Encrypted photo/secret vault. Files are stored AES-GCM encrypted in app-private dir. */
class VaultRepository(
    private val context: Context,
    private val prefs: VaultPreferences
) {

    private fun vaultDir(): File =
        File(context.filesDir, "vault").apply { if (!exists()) mkdirs() }

    suspend fun import(name: String, plainBytes: ByteArray): Boolean = withContext(Dispatchers.IO) {
        try {
            val key = prefs.ensureConfigured()
            val envelope = VaultCrypto.encrypt(plainBytes, key)
            val file = File(vaultDir(), sanitize(name) + ".vlt")
            file.outputStream().use { out ->
                out.write(envelope.iv.size)
                out.write(envelope.iv)
                out.write(envelope.cipherText)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun listEntries(): List<VaultEntry> = withContext(Dispatchers.IO) {
        vaultDir().listFiles()?.filter { it.name.endsWith(".vlt") }?.map {
            VaultEntry(it.name.removeSuffix(".vlt"), it.length())
        } ?: emptyList()
    }

    suspend fun decrypt(name: String): ByteArray? = withContext(Dispatchers.IO) {
        try {
            val key = prefs.getSecretKey() ?: return@withContext null
            val file = File(vaultDir(), sanitize(name) + ".vlt")
            if (!file.exists()) return@withContext null
            file.inputStream().use { ins ->
                val ivLen = ins.read()
                val iv = ByteArray(ivLen).also { ins.read(it) }
                val cipherText = ins.readBytes()
                VaultCrypto.decrypt(VaultCrypto.Envelope(iv, cipherText), key)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun delete(name: String) {
        withContext(Dispatchers.IO) {
            File(vaultDir(), sanitize(name) + ".vlt").delete()
        }
    }

    private fun sanitize(name: String): String =
        name.replace(Regex("[^A-Za-z0-9_.-]"), "_")
}