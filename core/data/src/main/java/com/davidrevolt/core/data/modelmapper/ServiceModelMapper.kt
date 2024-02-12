package com.davidrevolt.core.data.modelmapper

import android.bluetooth.BluetoothGattService
import com.davidrevolt.core.data.utils.asName


//TODO: Crate SERVICE UUID TO NAME CONVERTER
fun BluetoothGattService.asCustomGattService() =
    com.davidrevolt.core.model.CustomGattService(
        uuid = this.uuid,
        name = this.uuid.asName(),
        characteristics = this.characteristics.map { bluetoothGattCharacteristic -> bluetoothGattCharacteristic.asCustomDeviceCharacteristics() }
    )