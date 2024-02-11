package com.davidrevolt.feature.control

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidrevolt.core.designsystem.components.LoadingWheel
import com.davidrevolt.core.designsystem.components.isRefreshing.IsRefreshing
import com.davidrevolt.core.designsystem.components.isRefreshing.rememberIsRefreshingState
import com.davidrevolt.core.designsystem.drawable.homeBanner


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ControlScreen(
    viewModel: ControlViewModel = hiltViewModel()
) {
    val uiState by viewModel.controlUiState.collectAsStateWithLifecycle()

    val connectToBleDevice = viewModel::connectToDeviceGatt
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is ControlUiState.Data -> {
                val data = (uiState as ControlUiState.Data)
                ControlScreenContent(
                    isScanning = data.isScanning,
                    scanResults = data.scanResults,
                    connectToBleDevice = connectToBleDevice
                )
            }

            is ControlUiState.Loading -> LoadingWheel()
        }
    }
}


@Composable
private fun ControlScreenContent(
    isScanning: Boolean,
    scanResults: List<ScanResult>,
    connectToBleDevice: (device: BluetoothDevice) -> Unit
) {
    IsRefreshing(
        isRefreshingText = "Scanning...",
        state = rememberIsRefreshingState(isRefreshing = isScanning),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
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


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                scanResults.forEach { scanResult ->
                    item {
                    }
                }
            }

        }
    }
}

