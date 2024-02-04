package com.davidrevolt.core.data.utils.snackbarmanager

import kotlinx.coroutines.flow.SharedFlow


/**
 * Utility for accessing snackbarHostState from ViewModels, etc.
 */

interface SnackbarManager {

    val message: SharedFlow<String>
    suspend fun snackbarMessage(snackbarMessage : String)
}