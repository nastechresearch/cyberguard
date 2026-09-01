package com.nastech.nia.service.applock

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Returns the currently focused foreground package using UsageStatsManager.
 */
object ForegroundAppDetector {

    suspend fun getForegroundPackage(context: Context): String? = withContext(Dispatchers.IO) {
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return@withContext null
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 10_000L
        val events: UsageEvents = usm.queryEvents(beginTime, endTime)
        var currentPackage: String? = null
        var latestEvent = 0L
        while (events.hasNextEvent()) {
            val event = UsageEvents.Event()
            events.getNextEvent(event)
            if (event.timeStamp >= latestEvent) {
                latestEvent = event.timeStamp
                currentPackage = if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                    event.eventType == UsageEvents.Event.ACTIVITY_PAUSED
                ) {
                    event.packageName
                } else {
                    currentPackage
                }
            }
        }
        currentPackage
    }
}