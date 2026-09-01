package com.nastech.nia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nastech.nia.data.local.prefs.OnboardingPreferences
import com.nastech.nia.ui.navigation.CyberGuardNavGraph
import com.nastech.nia.ui.screens.onboard.OnboardingScreen
import com.nastech.nia.ui.theme.CyberGuardTheme
import com.nastech.nia.ui.theme.AmoledBlack
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CyberGuardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AmoledBlack
                ) {
                    val onboardingPrefs = remember {
                        OnboardingPreferences(applicationContext)
                    }
                    val scope = rememberCoroutineScope()
                    var showOnboarding by remember { mutableStateOf(false) }
                    var loading by remember { mutableStateOf(true) }

                    LaunchedEffect(Unit) {
                        showOnboarding = !onboardingPrefs.isCompleted()
                        loading = false
                    }

                    val completeOnboarding: () -> Unit = {
                        scope.launch {
                            onboardingPrefs.markCompleted()
                        }
                        showOnboarding = false
                    }

                    when {
                        loading -> Unit
                        showOnboarding -> OnboardingScreen(onComplete = completeOnboarding)
                        else -> CyberGuardNavGraph()
                    }
                }
            }
        }
    }
}