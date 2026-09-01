package com.nastech.nia.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector
import com.nastech.nia.R

sealed class Screen(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    data object Home : Screen("home", R.string.nav_home, Icons.Filled.Shield)
    data object Scanner : Screen("scanner", R.string.nav_scanner, Icons.Filled.Security)
    data object AntiTheft : Screen("antitheft", R.string.nav_antitheft, Icons.Filled.LocationOn)
    data object AppLock : Screen("applock", R.string.nav_applock, Icons.Filled.Lock)
    data object Settings : Screen("settings", R.string.nav_settings, Icons.Filled.Settings)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Scanner,
    Screen.AntiTheft,
    Screen.AppLock,
    Screen.Settings
)