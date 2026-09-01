package com.nastech.nia.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.nastech.nia.data.model.InstalledApp
import com.nastech.nia.data.model.ThreatLevel
import com.nastech.nia.data.model.ScanResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 * Scanner that enumerates installed launcher apps and performs a signature/behaviour
 * "threat profile" check. Heuristic engine is intentionally lightweight and local —
 * production builds would wire a real signature DB.
 */
class ScannerRepository(private val context: Context) {

    fun installedApps(): Flow<List<InstalledApp>> = flow {
        emit(withContext(Dispatchers.IO) { queryLauncherApps() })
    }

    /** Simulated scan streaming progress then a final result list. */
    fun scanApps(): Flow<Triple<Int, Int, List<ScanResult>>> = flow {
        val apps = withContext(Dispatchers.IO) { queryLauncherApps() }
        val total = apps.size
        val results = mutableListOf<ScanResult>()
        apps.forEachIndexed { index, app ->
            delay(60)
            val res = analyze(app)
            results += res
            emit(Triple(index + 1, total, results.toList()))
        }
    }

    private fun analyze(app: InstalledApp): ScanResult {
        val pkg = app.packageName
        val installed = context.packageManager.getInstalledPackages(0)
            .firstOrNull { it.packageName == pkg }
        val requestedPermissions = installed?.requestedPermissions?.toList().orEmpty()
        val threat = ScannerLogic.classify(pkg, requestedPermissions)
        val desc = ScannerLogic.describe(threat)
        return ScanResult(app.packageName, app.appName, threat, desc)
    }

    private fun queryLauncherApps(): List<InstalledApp> {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
        val ri = if (android.os.Build.VERSION.SDK_INT >= 33) {
            pm.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            pm.queryIntentActivities(intent, 0)
        }
        return ri.asSequence()
            .map { r ->
                val pkg = r.activityInfo?.packageName ?: return@map null
                if (pkg == context.packageName) return@map null
                InstalledApp(pkg, r.loadLabel(pm).toString(), r.loadIcon(pm))
            }
            .filterNotNull()
            .distinctBy { it.packageName }
            .sortedBy { it.appName.lowercase() }
            .toList()
    }
}