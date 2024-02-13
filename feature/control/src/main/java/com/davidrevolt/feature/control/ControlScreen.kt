package com.davidrevolt.feature.control

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidrevolt.core.ble.model.CustomGattCharacteristics
import com.davidrevolt.core.ble.model.CustomGattService
import com.davidrevolt.core.designsystem.components.LoadingWheel
import java.util.UUID


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ControlScreen(
    viewModel: ControlViewModel = hiltViewModel()
) {
    val uiState by viewModel.controlUiState.collectAsStateWithLifecycle()
    val connectToDeviceGatt = viewModel::connectToDeviceGatt
    val disconnectFromGatt = viewModel::disconnectFromGatt
    val onCharacteristicClick = viewModel::readCharacteristic

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
                    onCharacteristicClick = onCharacteristicClick
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
    disconnectFromGatt: () -> Unit,
    onCharacteristicClick: (serviceUUID: UUID, characteristicUUID: UUID) -> Unit,
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
/*                item {
                    val characteristicsTable = service.characteristics.joinToString(
                        separator = "\n|--",
                        prefix = "|--"
                    ) { it.uuid.toString() }
                    Text(
                        text = "\nService ${service.uuid}\n" +
                                "Characteristics:\n" +
                                characteristicsTable
                    )
                }*/
                item {
                    Text(text = "Service UUID: ${service.uuid}")
                    Text(text = "Service Name: ${service.name}")
                    service.characteristics.forEach { characteristic ->
                        CharacteristicDetails(
                            modifier = Modifier.clickable(onClick = {
                                onCharacteristicClick(
                                    characteristic.uuid,
                                    service.uuid
                                )
                            }),
                            characteristic = characteristic
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun CharacteristicDetails(
    modifier: Modifier = Modifier,
    characteristic: CustomGattCharacteristics
) {
    OutlinedCard(modifier = modifier) {
        var state by remember { mutableIntStateOf(0) } //0-READ, 1-WRITE
        var expanded by remember { mutableStateOf(false) }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(.85f)
                ) {

                    Text(
                        text = characteristic.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = characteristic.uuid.toString().uppercase(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = characteristic.properties.joinToString(", ") { it.name },
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = characteristic.readBytes?.toString(Charsets.UTF_8) ?: "no Value",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                ReadWriteMenu(
                    expanded = expanded,
                    onExpanded = { expanded = it },
                    onState = { state = it })
            }

            Spacer(modifier = Modifier.height(10.dp))


        }
    }
}

@Composable
fun ReadWriteMenu(
    expanded: Boolean,
    onExpanded: (Boolean) -> Unit,
    onState: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { onExpanded(true) }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Actions"
            )
        }
        DropdownMenu(
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.primaryContainer
            ),
            expanded = expanded,
            onDismissRequest = { onExpanded(false) }
        ) {
            DropdownMenuItem(
                //modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                //enabled = char.canRead,
                text = { Text("Read") },
                onClick = {
                    onState(0)
                    onExpanded(false)
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "Read"
                    )
                })
            Divider(color = MaterialTheme.colorScheme.primaryContainer)
            DropdownMenuItem(
                //modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                //enabled = char.canWrite,
                text = { Text("Write") },
                onClick = {
                    onState(1)
                    onExpanded(false)
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null
                    )
                })
        }
    }
}

