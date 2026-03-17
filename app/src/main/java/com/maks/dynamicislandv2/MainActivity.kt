package com.maks.dynamicislandv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maks.dynamicislandv2.ui.navigation.AppDestinations
import com.maks.dynamicislandv2.ui.screens.AboutScreen
import com.maks.dynamicislandv2.ui.screens.MainScreen
import com.maks.dynamicislandv2.ui.screens.SettingsScreen
import com.maks.dynamicislandv2.ui.screens.SetupScreen
import com.maks.dynamicislandv2.ui.screens.SplashScreen
import com.maks.dynamicislandv2.ui.theme.DynamicIslandV2Theme
import com.maks.dynamicislandv2.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DynamicIslandV2Theme {
                val navController = rememberNavController()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) { viewModel.refreshPermissions() }

                NavHost(navController = navController, startDestination = AppDestinations.Splash.route) {
                    composable(AppDestinations.Splash.route) {
                        SplashScreen(
                            onContinue = {
                                navController.navigate(
                                    if (uiState.setupComplete) AppDestinations.Main.route else AppDestinations.Setup.route
                                ) { popUpTo(AppDestinations.Splash.route) { inclusive = true } }
                            }
                        )
                    }
                    composable(AppDestinations.Setup.route) {
                        SetupScreen(
                            status = uiState.permissionStatus,
                            onRefresh = { viewModel.refreshPermissions() },
                            onGrantOverlay = { viewModel.openOverlayPermission(this@MainActivity) },
                            onGrantNotification = { viewModel.openNotificationAccess(this@MainActivity) },
                            onGrantBattery = { viewModel.openBatteryOptimization(this@MainActivity) },
                            onContinue = {
                                navController.navigate(AppDestinations.Main.route) {
                                    popUpTo(AppDestinations.Setup.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(AppDestinations.Main.route) {
                        MainScreen(
                            uiState = uiState,
                            onToggleEnabled = viewModel::setEnabled,
                            onShowTestIsland = viewModel::showTestIsland,
                            onReset = viewModel::resetIslandPosition,
                            onStartService = { viewModel.startOverlayService() },
                            onPermissions = { navController.navigate(AppDestinations.Setup.route) },
                            onSettings = { navController.navigate(AppDestinations.Settings.route) },
                            onAbout = { navController.navigate(AppDestinations.About.route) },
                            onRefresh = viewModel::refreshPermissions
                        )
                    }
                    composable(AppDestinations.Settings.route) {
                        SettingsScreen(
                            state = uiState.islandState,
                            onSetSize = viewModel::setSize,
                            onHideSensitive = viewModel::setHideSensitive,
                            onAutoCollapse = viewModel::setAutoCollapseMs,
                            onShowTestIsland = viewModel::showTestIsland,
                            onReset = viewModel::resetIslandPosition,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(AppDestinations.About.route) {
                        AboutScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
