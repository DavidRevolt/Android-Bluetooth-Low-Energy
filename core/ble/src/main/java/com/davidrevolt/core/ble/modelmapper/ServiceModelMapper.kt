package com.davidrevolt.core.ble.modelmapper

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import com.davidrevolt.core.ble.model.DeviceCharacteristicsDetails
import com.davidrevolt.core.ble.model.DeviceServiceDetails

//TODO: Make name method and characteristics converter
fun BluetoothGattService.asDeviceService()=
    DeviceServiceDetails(uuid = uuid, name = "",bluetoothGattService = this,characteristics= emptyList() )

fun BluetoothGattCharacteristic.asDeviceCharacteristics()=
    DeviceCharacteristicsDetails(uuid = uuid, name = "",canRead = false,canWrite = false,readBytes = null)