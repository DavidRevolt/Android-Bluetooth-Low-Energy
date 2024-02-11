package com.davidrevolt.feature.home

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidrevolt.core.ble.BluetoothLe
import com.davidrevolt.core.data.repository.BluetoothLowEnergyRepository
import com.davidrevolt.core.data.utils.snackbarmanager.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bluetoothLowEnergyRepository: BluetoothLowEnergyRepository,
    private val ble: BluetoothLe,
    private val snackbarManager: SnackbarManager
) : ViewModel() {


    val homeUiState = combine(ble.isScanning(), ble.getScanResults()) { isScanning, scanResults ->
        HomeUiState.Data(isScanning = isScanning, scanResults = scanResults)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )

    fun startBluetoothLeScan() {
        viewModelScope.launch {
            try {
                ble.startBluetoothLeScan()
            } catch (e: Exception) {
                Log.e("AppLog", "${e.message}")
                snackbarManager.snackbarMessage("${e.message}")
            }
        }
    }

    fun stopBluetoothLeScan() {
        viewModelScope.launch {
            try {
                ble.stopBluetoothLeScan()
            } catch (e: Exception) {
                Log.e("AppLog", "${e.message}")
                snackbarManager.snackbarMessage("${e.message}")
            }
        }
    }

    fun connectToDeviceGatt(device: BluetoothDevice) {
        viewModelScope.launch {
            try {
                ble.connectToDeviceGatt(device = device)
            } catch (e: Exception) {
                Log.e("AppLog", "${e.message}")
                snackbarManager.snackbarMessage("${e.message}")
            }
        }
    }

    fun disconnectFromGatt(bluetoothGatt: BluetoothGatt) {
        viewModelScope.launch {
            try {
                ble.disconnectFromGatt(bluetoothGatt = bluetoothGatt)
            } catch (e: Exception) {
                Log.e("AppLog", "${e.message}")
                snackbarManager.snackbarMessage("${e.message}")
            }
        }
    }
}

sealed interface HomeUiState {
    data class Data(val isScanning: Boolean, val scanResults: List<ScanResult>) :
        HomeUiState

    data object Loading : HomeUiState
}