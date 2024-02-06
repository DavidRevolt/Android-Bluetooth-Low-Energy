package com.davidrevolt.feature.home

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidrevolt.core.designsystem.components.AppFabButton
import com.davidrevolt.core.designsystem.components.LoadingWheel
import com.davidrevolt.core.designsystem.components.isSyncing.IsSyncing
import com.davidrevolt.core.designsystem.components.isSyncing.rememberIsSyncingState
import com.davidrevolt.core.designsystem.drawable.homeBanner
import com.davidrevolt.core.designsystem.icons.AppIcons
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val showHomeDialog = remember { mutableStateOf(false) }
    val onMainFabClick = { showHomeDialog.value = true }
    val startBluetoothLeScan = viewModel::startBluetoothLeScan

    if (showHomeDialog.value)
        HomeDialog(
            showHomeDialog = showHomeDialog,
            onButtonClick = {}
        )

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AppFabButton(onFabClick = onMainFabClick, icon = AppIcons.Add)
            }

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = homeBanner), contentDescription = "home banner",
                modifier = Modifier
                    .padding(top = 30.dp)
                    .size(200.dp)
            )
            when (uiState) {
                is HomeUiState.Data -> {
                    val data = (uiState as HomeUiState.Data)
                    if (data.stringsData.isNotEmpty())
                        HomeScreenContent(data.isSyncing, data.stringsData, startBluetoothLeScan)
                    else
                        Text("Nothing to show here...yet", color = Color.White)
                }

                is HomeUiState.Loading -> LoadingWheel()
            }
        }
    }
}


// First we ask BLE Permissions
// When permissions granted we then ask to enable Bluetooth to start scanning

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HomeScreenContent(
    isSyncing: Boolean,
    stringsData: List<String>,
    startBluetoothLeScan: () -> Unit
) {
    val bluetoothAdapter: BluetoothAdapter? =
        getSystemService(LocalContext.current, BluetoothManager::class.java)?.adapter

    val blePermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // Enable Bluetooth Intent
    val onBluetoothEnableLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // If Bluetooth enabled: Activate scan Method HERE!
            if (result.resultCode == Activity.RESULT_OK) {
                startBluetoothLeScan.invoke()
            }
        }

    // Request BLE Permissions Intent
    val onBlePermissionsGrantedLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (blePermissionsState.allPermissionsGranted) {
                // If Permission Granted: Activate scan Method HERE!
                if (bluetoothAdapter == null) {
                    Log.d("AppLog","Device doesn't support Bluetooth")
                }else{
                    //Activate Bluetooth
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    onBluetoothEnableLauncher.launch(enableBtIntent)
                }

            }
        }



    val onScanClick = {
        onBlePermissionsGrantedLauncher.launch(blePermissionsState.permissions.map { it.permission }
            .toTypedArray())
    }

    IsSyncing(
        state = rememberIsSyncingState(isRefreshing = isSyncing),
        onRefresh = {},
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // Home Content

            if (blePermissionsState.shouldShowRationale) {
                val context = LocalContext.current
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                Button(onClick = { ContextCompat.startActivity(context, intent, null) }) {
                    Text("Permissions denied before, Go to settings and allow them!")
                }
            }
            // Alert 4 user to ask 4 permissions
            if (!blePermissionsState.allPermissionsGranted && !blePermissionsState.shouldShowRationale)
                Text("You should ask 4 permissions...")
            Button(onClick = onScanClick) {
                Text("Start scan")
            }
            LazyColumn {
                item {
                    stringsData.forEach { string -> Text(text = string) }
                }
            }
        }
    }
}


@Composable
fun HomeDialog(
    showHomeDialog: MutableState<Boolean>,
    onButtonClick: () -> Unit,
) {

    Dialog(
        onDismissRequest = { showHomeDialog.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Dialog Content
                Text("Test")
                Button(onClick = {
                    onButtonClick()
                    showHomeDialog.value = false
                }) {
                    Text("onButtonClick")
                }
            }
        }
    }
}