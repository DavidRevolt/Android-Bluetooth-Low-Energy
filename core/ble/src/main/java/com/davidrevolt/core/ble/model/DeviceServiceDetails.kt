package com.davidrevolt.core.ble.model

import android.bluetooth.BluetoothGattService
import java.util.UUID

data class DeviceServiceDetails(
    val uuid: UUID,
    val name: String,
    val bluetoothGattService: BluetoothGattService,
    val characteristics: List<DeviceCharacteristicsDetails>
)

