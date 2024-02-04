package com.davidrevolt.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.davidrevolt.app.navigation.AppNavigation
import com.davidrevolt.core.data.utils.snackbarmanager.SnackbarManager
import com.davidrevolt.core.designsystem.components.rememberSystemUiController

@Composable
fun App(
    snackbarManager: SnackbarManager,
    appState: AppState = rememberAppState()
) {

    val systemUiController = rememberSystemUiController()
    systemUiController.systemBarsDarkContentEnabled = true

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        snackbarManager.message.collect { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = null,
                duration = SnackbarDuration.Short,
            )
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState, modifier = Modifier.safeDrawingPadding()) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppNavigation(appState = appState)
        }
    }
}
