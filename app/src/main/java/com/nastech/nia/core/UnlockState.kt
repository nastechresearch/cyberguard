package com.nastech.nia.core

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Shared in-memory state of which packages are currently "unlocked" (recently
 * authenticated) and should not prompt the lock screen. Used by both the
 * detection service and the lock activity.
 */
object UnlockState {

    private data class Entry(val expiresAt: Long)

    private val mutex = Mutex()
    private val unlocked = mutableMapOf<String, Entry>()

    suspend fun isUnlocked(packageName: String): Boolean = mutex.withLock {
        val entry = unlocked[packageName] ?: return false
        if (System.currentTimeMillis() > entry.expiresAt) {
            unlocked.remove(packageName)
            false
        } else {
            true
        }
    }

    suspend fun markUnlocked(packageName: String, durationMinutes: Int) {
        val minutes = if (durationMinutes <= 0) 1 else durationMinutes
        mutex.withLock {
            unlocked[packageName] = Entry(
                expiresAt = System.currentTimeMillis() + minutes * 60_000L
            )
        }
    }

    suspend fun clear() {
        mutex.withLock { unlocked.clear() }
    }
}