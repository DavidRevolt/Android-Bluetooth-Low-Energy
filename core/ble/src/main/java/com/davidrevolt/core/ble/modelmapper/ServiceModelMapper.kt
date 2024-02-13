package com.davidrevolt.core.ble.modelmapper

import android.bluetooth.BluetoothGattService
import com.davidrevolt.core.ble.model.CustomGattService


//TODO: Crate SERVICE UUID TO NAME CONVERTER
fun BluetoothGattService.asCustomGattService() =
    CustomGattService(
        uuid = this.uuid,
        name = this.uuid.asName(),
        characteristics = this.characteristics.map { bluetoothGattCharacteristic -> bluetoothGattCharacteristic.asCustomDeviceCharacteristics() }
    )