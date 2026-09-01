package com.nastech.nia.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nastech.nia.ui.screens.home.HomeScreen
import com.nastech.nia.ui.screens.applock.AppLockScreen
import com.nastech.nia.ui.screens.antitheft.AntiTheftScreen
import com.nastech.nia.ui.screens.scanner.ScannerScreen
import com.nastech.nia.ui.screens.settings.SettingsScreen
import com.nastech.nia.ui.screens.vault.VaultScreen
import com.nastech.nia.ui.screens.wifi.WifiScanScreen
import com.nastech.nia.ui.screens.passwords.PasswordCheckScreen
import com.nastech.nia.ui.screens.privacy.PrivacyScreen
import com.nastech.nia.ui.screens.junkcleaner.JunkCleanerScreen
import com.nastech.nia.ui.theme.NeonCyan
import com.nastech.nia.ui.theme.SurfaceDark
import com.nastech.nia.ui.theme.TextSecondary

@Composable
fun CyberGuardNavGraph() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                contentColor = TextSecondary
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(screen.labelRes),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            selectedTextColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = NeonCyan.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onQuickScan = { navController.navigate(Screen.Scanner.route) },
                    onFullScan = { navController.navigate(Screen.Scanner.route) },
                    onOpenTool = { route -> navController.navigate(route) }
                )
            }
            composable(Screen.Scanner.route) {
                ScannerScreen()
            }
            composable(Screen.AntiTheft.route) {
                AntiTheftScreen()
            }
            composable(Screen.AppLock.route) {
                AppLockScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(Screen.Vault.route) {
                VaultScreen()
            }
            composable(Screen.Wifi.route) {
                WifiScanScreen()
            }
            composable(Screen.Passwords.route) {
                PasswordCheckScreen()
            }
            composable(Screen.Privacy.route) {
                PrivacyScreen()
            }
            composable(Screen.JunkCleaner.route) {
                JunkCleanerScreen()
            }
        }
    }
}