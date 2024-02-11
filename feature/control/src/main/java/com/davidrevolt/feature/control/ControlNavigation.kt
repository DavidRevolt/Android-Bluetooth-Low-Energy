package com.davidrevolt.feature.control

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val CONTROL_ROUTE = "control_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(CONTROL_ROUTE, navOptions)
}

@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.controlScreen() {
    composable(route = CONTROL_ROUTE) {
        ControlScreen()
    }
}