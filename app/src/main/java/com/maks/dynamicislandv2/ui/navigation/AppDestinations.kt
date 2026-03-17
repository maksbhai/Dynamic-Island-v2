package com.maks.dynamicislandv2.ui.navigation

sealed class AppDestinations(val route: String) {
    data object Splash : AppDestinations("splash")
    data object Setup : AppDestinations("setup")
    data object Main : AppDestinations("main")
    data object Settings : AppDestinations("settings")
    data object About : AppDestinations("about")
}
