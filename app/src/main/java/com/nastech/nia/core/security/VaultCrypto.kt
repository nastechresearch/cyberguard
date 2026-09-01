package com.nastech.nia.core.security

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/** AES-GCM encryption helper for the secure vault. */
object VaultCrypto {

    private const val GCM_TAG_BITS = 128
    private const val IV_SIZE = 12

    fun generateKey(): SecretKey =
        KeyGenerator.getInstance("AES").run {
            init(256)
            generateKey()
        }

    fun encrypt(data: ByteArray, key: SecretKey): Envelope {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(IV_SIZE).also { SecureRandom().nextBytes(it) }
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(GCM_TAG_BITS, iv))
        val encrypted = cipher.doFinal(data)
        return Envelope(iv, encrypted)
    }

    fun decrypt(envelope: Envelope, key: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(
            Cipher.DECRYPT_MODE,
            key,
            GCMParameterSpec(GCM_TAG_BITS, envelope.iv)
        )
        return cipher.doFinal(envelope.cipherText)
    }

    data class Envelope(val iv: ByteArray, val cipherText: ByteArray)
}