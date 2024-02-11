package com.davidrevolt.core.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanResult
import kotlinx.coroutines.flow.Flow

interface BluetoothLe{
    fun startBluetoothLeScan()
    fun stopBluetoothLeScan()
    fun isScanning(): Flow<Boolean>
    fun getScanResults(): Flow<List<ScanResult>>

    fun connectToDeviceGatt(device: BluetoothDevice)
    fun disconnectFromGatt(bluetoothGatt: BluetoothGatt)
}