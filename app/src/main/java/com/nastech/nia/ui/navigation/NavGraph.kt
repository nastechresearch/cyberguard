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
import com.nastech.nia.ui.components.PlaceholderScreen
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
                HomeScreen()
            }
            composable(Screen.Scanner.route) {
                PlaceholderScreen(
                    icon = Screen.Scanner.icon,
                    title = "Scanner",
                    subtitle = "App scanner launching in Phase 2",
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(Screen.AntiTheft.route) {
                PlaceholderScreen(
                    icon = Screen.AntiTheft.icon,
                    title = "Anti-Theft",
                    subtitle = "Device protection launching in Phase 3",
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(Screen.AppLock.route) {
                PlaceholderScreen(
                    icon = Screen.AppLock.icon,
                    title = "App Lock",
                    subtitle = "App Lock launching in Phase 1",
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(Screen.Settings.route) {
                PlaceholderScreen(
                    icon = Screen.Settings.icon,
                    title = "Settings",
                    subtitle = "Settings coming soon",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}