package com.davidrevolt.feature.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidrevolt.core.designsystem.components.AppFabButton
import com.davidrevolt.core.designsystem.components.LoadingWheel
import com.davidrevolt.core.designsystem.components.isRefreshing.IsRefreshing
import com.davidrevolt.core.designsystem.components.isRefreshing.rememberIsRefreshingState
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

    val startBluetoothLeScan = viewModel::startBluetoothLeScan
    val stopBluetoothLeScan = viewModel::stopBluetoothLeScan
    val connectToBleDevice = viewModel::connectToDeviceGatt
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is HomeUiState.Data -> {
                val data = (uiState as HomeUiState.Data)
                HomeScreenContent(
                    isScanning = data.isScanning,
                    scanResults = data.scanResults,
                    startBluetoothLeScan = startBluetoothLeScan,
                    stopBluetoothLeScan = stopBluetoothLeScan,
                    connectToBleDevice = connectToBleDevice
                )
            }

            is HomeUiState.Loading -> LoadingWheel()
        }
    }
}


@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HomeScreenContent(
    isScanning: Boolean,
    scanResults: List<ScanResult>,
    startBluetoothLeScan: () -> Unit,
    stopBluetoothLeScan: () -> Unit,
    connectToBleDevice: (device: BluetoothDevice) -> Unit
) {
    val context = LocalContext.current
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
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
            if (blePermissionsState.allPermissionsGranted) {
                // If Permission Granted: Activate scan Method HERE!
                if (bluetoothAdapter == null) {
                    Toast.makeText(context, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("AppLog", "Device doesn't support Bluetooth")
                } else {
                    //Activate Bluetooth
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    onBluetoothEnableLauncher.launch(enableBtIntent) // GO TO Enable Bluetooth Intent
                }
            }
        }

    val onPermissionsCheckAndScan =
        { // Request BLE Permissions -> Request to Enable Bluetooth -> SCAN!
            onBlePermissionsGrantedLauncher.launch(blePermissionsState.permissions.map { it.permission }
                .toTypedArray())
        }

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AppFabButton(
                onFabClick = if (isScanning) stopBluetoothLeScan else onPermissionsCheckAndScan,
                icon = if (isScanning) AppIcons.Stop else AppIcons.Search,
                containerColor = if (isScanning) MaterialTheme.colorScheme.error else FloatingActionButtonDefaults.containerColor
            )
        }
    ) { innerPadding ->
        IsRefreshing(
            isRefreshingText = "Scanning...",
            state = rememberIsRefreshingState(isRefreshing = isScanning),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Home Content
                Image(
                    painter = painterResource(id = homeBanner), contentDescription = "home banner",
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .size(200.dp)
                )
                if (blePermissionsState.shouldShowRationale) {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    Button(onClick = { ContextCompat.startActivity(context, intent, null) }) {
                        Text("Permissions denied before, Go to settings and allow them!")
                    }
                }
                // Text 4 user to notify he needs to allow permissions
                if (!blePermissionsState.allPermissionsGranted && !blePermissionsState.shouldShowRationale)
                    Text("You should allow permissions...")

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    scanResults.forEach { scanResult ->
                        item {
                            Button(onClick = { connectToBleDevice(scanResult.device) }) {
                                Text(text = "Name: ${scanResult.device.name} ADD: ${scanResult.device.address} RSSI: ${scanResult.rssi}")
                            }
                        }
                    }

                }
            }
        }
    }
}
