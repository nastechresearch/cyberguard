package com.nastech.nia.core.security

import java.io.File

/** Junk/temporary file classification for the cleaner. Pure logic. */
object JunkFileLogic {

    enum class Type { CACHE, TEMP, LOG, IMAGE_DUP, OTHER }

    val CACHE_DIRS = listOf("cache", "tmp", "temp", "thumbnails")
    val IMAGE_EXTENSIONS = setOf("jpg", "jpeg", "png", "gif", "webp", "bmp")

    fun classify(path: String): Type {
        val lower = path.lowercase()
        val dir = lower.substringBeforeLast('/', "")
        return when {
            CACHE_DIRS.any { dir.contains(it) } -> Type.CACHE
            lower.endsWith(".tmp") || lower.endsWith(".bak") -> Type.TEMP
            lower.endsWith(".log") -> Type.LOG
            lower.substringAfterLast('.').takeIf { it in IMAGE_EXTENSIONS } != null -> Type.IMAGE_DUP
            else -> Type.OTHER
        }
    }

    fun humanSize(bytes: Long): String = when {
        bytes >= 1024 * 1024 * 1024 -> String.format(java.util.Locale.US, "%.1f GB", bytes / (1024.0 * 1024 * 1024))
        bytes >= 1024 * 1024 -> String.format(java.util.Locale.US, "%.1f MB", bytes / (1024.0 * 1024))
        bytes >= 1024 -> String.format(java.util.Locale.US, "%.1f KB", bytes / 1024.0)
        else -> "$bytes B"
    }

    fun isImage(path: String): Boolean =
        path.substringAfterLast('.', "").lowercase() in IMAGE_EXTENSIONS
}