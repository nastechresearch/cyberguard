package com.nastech.nia.service.applock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nastech.nia.ui.theme.CyberGuardTheme
import com.nastech.nia.ui.theme.AmoledBlack

class LockActivity : ComponentActivity() {

    private val targetPackage: String
        get() = intent.getStringExtra(EXTRA_PACKAGE) ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppLockAppContextHolder.context = applicationContext
        setContent {
            CyberGuardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AmoledBlack
                ) {
                    val vm: LockViewModel = viewModel()
                    LockScreen(
                        packageName = targetPackage,
                        viewModel = vm,
                        onUnlocked = { finish() },
                        onDismiss = { finish() }
                    )
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (!isFinishing) finish()
    }

    companion object {
        const val EXTRA_PACKAGE = "package_name"
    }
}