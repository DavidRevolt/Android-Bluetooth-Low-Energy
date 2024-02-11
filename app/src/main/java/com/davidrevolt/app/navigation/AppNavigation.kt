package com.davidrevolt.app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.davidrevolt.app.ui.AppState
import com.davidrevolt.feature.home.HOME_ROUTE
import com.davidrevolt.feature.home.homeScreen

@RequiresApi(Build.VERSION_CODES.S)
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