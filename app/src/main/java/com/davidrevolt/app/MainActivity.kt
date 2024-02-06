package com.davidrevolt.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.davidrevolt.app.ui.App
import com.davidrevolt.app.ui.theme.AppTheme
import com.davidrevolt.core.data.utils.snackbarmanager.SnackbarManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var snackbarManager: SnackbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val locale = Locale("en")
        Locale.setDefault(locale)

        setContent {
            AppTheme {
                val context = LocalContext.current
                val resources = context.resources
                val configuration = resources.configuration
                configuration.setLocale(locale)
                resources.updateConfiguration(configuration, resources.displayMetrics)
                LocalContext.provides(context.createConfigurationContext(configuration))
                App(snackbarManager = snackbarManager)
            }
        }
    }
}