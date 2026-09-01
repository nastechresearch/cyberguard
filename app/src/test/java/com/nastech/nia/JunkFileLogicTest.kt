package com.nastech.nia

import com.nastech.nia.core.security.JunkFileLogic
import com.nastech.nia.core.security.JunkFileLogic.Type
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JunkFileLogicTest {

    @Test
    fun cacheFiles_classified() {
        assertEquals(Type.CACHE, JunkFileLogic.classify("/data/data/com.app/cache/data.txt"))
        assertEquals(Type.CACHE, JunkFileLogic.classify("/data/user/0/com.app/tmp/x.tmp"))
    }

    @Test
    fun logAndTempFiles_classified() {
        assertEquals(Type.TEMP, JunkFileLogic.classify("/logs/app.log.tmp"))
        assertEquals(Type.CACHE, JunkFileLogic.classify("/cache/temp.dat"))
        assertEquals(Type.TEMP, JunkFileLogic.classify("/downloads/setup.tmp"))
    }

    @Test
    fun imageDetection() {
        assertTrue(JunkFileLogic.isImage("/pics/photo.jpg"))
        assertTrue(JunkFileLogic.isImage("IMG_2026.PNG"))
        assertEquals(false, JunkFileLogic.isImage("document.pdf"))
    }

    @Test
    fun humanSize_formats() {
        assertEquals("1.0 KB", JunkFileLogic.humanSize(1024))
        assertEquals("1.0 MB", JunkFileLogic.humanSize(1024 * 1024))
        assertEquals("512 B", JunkFileLogic.humanSize(512))
    }
}