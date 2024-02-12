package com.davidrevolt.feature.control

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidrevolt.core.designsystem.components.LoadingWheel
import com.davidrevolt.core.model.CustomGattService


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ControlScreen(
    viewModel: ControlViewModel = hiltViewModel()
) {
    val uiState by viewModel.controlUiState.collectAsStateWithLifecycle()
    val connectToDeviceGatt = viewModel::connectToDeviceGatt
    val disconnectFromGatt = viewModel::disconnectFromGatt
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is ControlUiState.Data -> {
                val data = (uiState as ControlUiState.Data)
                ControlScreenContent(
                    connectionState = data.connectionState,
                    deviceServices = data.deviceServices,
                    connectToDeviceGatt = connectToDeviceGatt,
                    disconnectFromGatt = disconnectFromGatt,
                )
            }

            is ControlUiState.Loading -> LoadingWheel()
        }
    }
}


@Composable
private fun ControlScreenContent(
    connectionState: String,
    deviceServices: List<CustomGattService>,
    connectToDeviceGatt: () -> Unit,
    disconnectFromGatt: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Home Content
        Text(text = connectionState)
        Button(onClick = connectToDeviceGatt) {
            Text(text = "Connect")
        }
        Button(onClick = disconnectFromGatt) {
            Text(text = "Disconnect")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            deviceServices.forEach { service ->
                item {
                    val characteristicsTable = service.characteristics.joinToString(
                        separator = "\n|--",
                        prefix = "|--"
                    ) { it.uuid.toString() }
                    Text(
                        text = "\nService ${service.uuid}\n" +
                                "Characteristics:\n" +
                                characteristicsTable
                    )
                }
            }
        }
    }
}


