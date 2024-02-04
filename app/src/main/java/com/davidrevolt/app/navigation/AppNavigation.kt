package com.davidrevolt.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.davidrevolt.app.ui.AppState
import com.davidrevolt.feature.home.HOME_ROUTE
import com.davidrevolt.feature.home.homeScreen

@Composable
fun AppNavigation(appState: AppState) {
    val navController = appState.navController
    val startDestination = HOME_ROUTE

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen()
    }
}