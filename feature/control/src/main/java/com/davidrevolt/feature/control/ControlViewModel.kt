package com.davidrevolt.feature.control

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidrevolt.core.ble.BluetoothLeService
import com.davidrevolt.core.ble.model.CustomGattService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ControlViewModel @Inject constructor(
    private val bluetoothLeService: BluetoothLeService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Get it from NavGraph
    private val bleDeviceName: String = checkNotNull(savedStateHandle[DEVICE_NAME])
    private val bleDeviceAddress: String = checkNotNull(savedStateHandle[DEVICE_ADDRESS])

    // Used to send msg to snackbar
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val controlUiState = combine(
        bluetoothLeService.getConnectionState(),
        bluetoothLeService.getDeviceServices()
    ) { connectionState, deviceServices ->
        ControlUiState.Data(
            deviceName = bleDeviceName,
            deviceAddress = bleDeviceAddress,
            connectionState = connectionState,
            deviceServices = deviceServices
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ControlUiState.Loading
        )

    init {
        connectToDeviceGatt()
    }

    fun connectToDeviceGatt() {
        viewModelScope.launch {
            try {
                bluetoothLeService.connectToDeviceGatt(deviceAddress = bleDeviceAddress)
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("${e.message}"))
            }
        }
    }

    fun readCharacteristic(characteristicUUID: UUID) {
        viewModelScope.launch {
            try {
                bluetoothLeService.readCharacteristic(characteristicUUID)
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("${e.message}"))
            }
        }
    }

    fun writeCharacteristic(characteristicUUID: UUID, value: ByteArray) {
        viewModelScope.launch {
            try {
                bluetoothLeService.writeCharacteristic(characteristicUUID, value)
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("${e.message}"))
            }
        }
    }

    fun readDescriptor(characteristicUUID: UUID, descriptorUUID: UUID) {
        viewModelScope.launch {
            try {
                bluetoothLeService.readDescriptor(characteristicUUID, descriptorUUID)
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("${e.message}"))
            }
        }
    }

    fun writeDescriptor(characteristicUUID: UUID, descriptorUUID: UUID, value: ByteArray) {
        viewModelScope.launch {
            try {
                bluetoothLeService.writeDescriptor(characteristicUUID, descriptorUUID, value)
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("${e.message}"))
            }
        }
    }


    fun enableCharacteristicNotifications(characteristicUUID: UUID) {
        viewModelScope.launch {
            try {
                bluetoothLeService.enableCharacteristicNotifications(characteristicUUID)
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("${e.message}"))
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        bluetoothLeService.disconnectFromGatt()
    }

}

sealed interface ControlUiState {
    data class Data(
        val deviceName: String,
        val deviceAddress: String,
        val connectionState: Int,
        val deviceServices: List<CustomGattService>
    ) :
        ControlUiState

    data object Loading : ControlUiState
}

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}
