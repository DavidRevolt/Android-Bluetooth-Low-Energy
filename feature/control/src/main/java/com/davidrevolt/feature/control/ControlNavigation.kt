package com.davidrevolt.feature.control

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val DEVICE_ADDRESS = "deviceAddress"
const val CONTROL_ROUTE = "control_route/deviceAddress={deviceAddress}"

fun NavController.navigateToControl(deviceAddress: String, navOptions: NavOptions? = null) {
    this.navigate("control_route/deviceAddress=${deviceAddress}", navOptions)
}

@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.controlScreen() {
    composable(route = CONTROL_ROUTE, arguments = listOf(navArgument(DEVICE_ADDRESS) {
        type = NavType.StringType
        defaultValue = ""
        //nullable = true  // if no args -> set query to null [not needed because defaultValue is set]
    }
    )) {
        ControlScreen()
    }
}