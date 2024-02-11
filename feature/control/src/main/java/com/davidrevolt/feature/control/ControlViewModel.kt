package com.davidrevolt.feature.control

import android.bluetooth.BluetoothGattService
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidrevolt.core.ble.BluetoothLe
import com.davidrevolt.core.data.utils.snackbarmanager.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ControlViewModel @Inject constructor(
    private val ble: BluetoothLe,
    savedStateHandle: SavedStateHandle,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val bleDeviceAddress: String = checkNotNull(savedStateHandle[DEVICE_ADDRESS])

    val controlUiState = combine(ble.getConnectionState(), ble.getDeviceServices()) { connectionState, deviceServices ->
        ControlUiState.Data(connectionState = connectionStateToReadableMsg(connectionState), deviceServices = deviceServices)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ControlUiState.Loading
        )


    fun connectToDeviceGatt() {
        viewModelScope.launch {
            try {
                ble.connectToDeviceGatt(deviceAddress = bleDeviceAddress)
            } catch (e: Exception) {
                Log.e("AppLog", "${e.message}")
                snackbarManager.snackbarMessage("${e.message}")
            }
        }
    }

    fun disconnectFromGatt() {
        viewModelScope.launch {
            try {
                ble.disconnectFromGatt()
            } catch (e: Exception) {
                Log.e("AppLog", "${e.message}")
                snackbarManager.snackbarMessage("${e.message}")
            }
        }
    }

    private fun connectionStateToReadableMsg(connectionState: Int): String=
         when(connectionState){
            0-> "Disconnected"
            1-> "Connecting..."
            2-> "Connected"
            3-> "Disconnecting..."
            else ->"Unknown connection state!!"
        }

}

sealed interface ControlUiState {
    data class Data(val connectionState: String, val deviceServices: List<BluetoothGattService>) :
        ControlUiState

    data object Loading : ControlUiState
}