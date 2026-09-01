package com.nastech.nia.data.repository

import android.content.Context
import com.nastech.nia.core.security.JunkFileLogic.humanSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class JunkItem(val path: String, val bytes: Long, val type: com.nastech.nia.core.security.JunkFileLogic.Type) {
    val label: String get() = path.substringAfterLast('/').ifBlank { path }
    val size: String get() = humanSize(bytes)
}

/**
 * Scans known cache/temp folders on the device and computes reclaimable size.
 * Pure safety: only targets standard cache dirs, never user documents.
 */
object JunkCleanerRepository {

    fun scan(context: Context): List<JunkItem> {
        val items = mutableListOf<JunkItem>()
        context.cacheDir?.walkTopDown()?.forEach { f ->
            if (f.isFile) {
                items += JunkItem(
                    path = f.absolutePath,
                    bytes = f.length(),
                    type = com.nastech.nia.core.security.JunkFileLogic.classify(f.absolutePath)
                )
            }
        }
        return items.take(500)
    }

    fun totalBytes(items: List<JunkItem>): Long = items.sumOf { it.bytes }

    fun delete(items: List<JunkItem>): Int {
        var freed = 0
        items.forEach { item ->
            val file = java.io.File(item.path)
            if (file.exists() && file.delete()) freed += item.bytes.toInt()
        }
        return freed
    }
}