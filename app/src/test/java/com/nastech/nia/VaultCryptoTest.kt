package com.nastech.nia

import com.nastech.nia.core.security.VaultCrypto
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class VaultCryptoTest {

    @Test
    fun encryptDecrypt_roundTrips() {
        val key = VaultCrypto.generateKey()
        val plaintext = "secret photo bytes".toByteArray()
        val envelope = VaultCrypto.encrypt(plaintext, key)
        assertFalse(plaintext.contentEquals(envelope.cipherText))
        val decrypted = VaultCrypto.decrypt(envelope, key)
        assertArrayEquals(plaintext, decrypted)
    }

    @Test
    fun samePlaintext_producesDifferentCipherText() {
        val key = VaultCrypto.generateKey()
        val data = "sensitive".toByteArray()
        val e1 = VaultCrypto.encrypt(data, key)
        val e2 = VaultCrypto.encrypt(data, key)
        assertFalse(e1.cipherText.contentEquals(e2.cipherText))
        assertFalse(e1.iv.contentEquals(e2.iv))
    }

    @Test(expected = javax.crypto.AEADBadTagException::class)
    fun wrongKey_failsToDecrypt() {
        val key1 = VaultCrypto.generateKey()
        val key2 = VaultCrypto.generateKey()
        val envelope = VaultCrypto.encrypt("data".toByteArray(), key1)
        VaultCrypto.decrypt(envelope, key2)
    }
}