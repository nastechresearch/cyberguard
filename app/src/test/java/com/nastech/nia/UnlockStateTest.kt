package com.nastech.nia

import com.nastech.nia.core.UnlockState
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UnlockStateTest {

    @Before
    fun setUp() = runBlocking {
        UnlockState.clear()
    }

    @Test
    fun notUnlockedByDefault() = runBlocking {
        assertFalse(UnlockState.isUnlocked("com.example.app"))
    }

    @Test
    fun markUnlocked_allowsAccess() = runBlocking {
        UnlockState.markUnlocked("com.example.app", 1)
        assertTrue(UnlockState.isUnlocked("com.example.app"))
    }

    @Test
    fun clear_revokesAccess() = runBlocking {
        UnlockState.markUnlocked("com.example.app", 1)
        UnlockState.clear()
        assertFalse(UnlockState.isUnlocked("com.example.app"))
    }

    @Test
    fun differentPackage_notUnlocked() = runBlocking {
        UnlockState.markUnlocked("com.example.app", 1)
        assertFalse(UnlockState.isUnlocked("com.other.app"))
    }
}