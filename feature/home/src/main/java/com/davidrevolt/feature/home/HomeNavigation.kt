package com.davidrevolt.feature.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HOME_ROUTE, navOptions)
}

@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.homeScreen(onScanResultClick: (deviceAddress: String) -> Unit) {
    composable(route = HOME_ROUTE) {
        HomeScreen(onScanResultClick = onScanResultClick)
    }
}