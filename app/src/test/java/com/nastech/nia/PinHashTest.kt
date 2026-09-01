package com.nastech.nia

import com.nastech.nia.data.repository.PinHash
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PinHashTest {

    @Test
    fun createAndVerify_returnsTrueForCorrectPin() {
        val result = PinHash.create("123456")
        assertTrue(PinHash.verify("123456", result.hash, result.salt))
    }

    @Test
    fun verify_returnsFalseForWrongPin() {
        val result = PinHash.create("123456")
        assertFalse(PinHash.verify("000000", result.hash, result.salt))
    }

    @Test
    fun create_producesUniqueSalts() {
        val a = PinHash.create("123456")
        val b = PinHash.create("123456")
        assertFalse(a.salt == b.salt)
        assertFalse(a.hash == b.hash)
    }

    @Test
    fun verify_returnsFalseForEmptyStoredHash() {
        assertFalse(PinHash.verify("123456", "", ""))
    }
}