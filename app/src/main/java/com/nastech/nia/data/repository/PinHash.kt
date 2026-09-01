package com.nastech.nia.data.repository

import java.security.MessageDigest
import java.security.SecureRandom

object PinHash {

    private const val HASH_ALGORITHM = "SHA-256"
    private const val ITERATIONS = 100_000
    private const val KEY_LENGTH = 32

    data class Result(val hash: String, val salt: String)

    fun create(pin: String): Result {
        val saltBytes = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val salt = saltBytes.toHex()
        val hash = pbkdf2(pin, saltBytes)
        return Result(hash = hash, salt = salt)
    }

    fun verify(pin: String, storedHash: String, saltHex: String): Boolean {
        if (storedHash.isEmpty() || saltHex.isEmpty()) return false
        val saltBytes = saltHex.fromHex()
        val hash = pbkdf2(pin, saltBytes)
        return MessageDigest.isEqual(hash.toByteArray(), storedHash.toByteArray())
    }

    private fun pbkdf2(pin: String, salt: ByteArray): String {
        val spec = javax.crypto.spec.PBEKeySpec(
            pin.toCharArray(),
            salt,
            ITERATIONS,
            KEY_LENGTH * 8
        )
        val factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return factory.generateSecret(spec).encoded.toHex()
    }

    private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }

    private fun String.fromHex(): ByteArray =
        chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}