package com.davidrevolt.core.ble

import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult
import kotlinx.coroutines.flow.Flow

interface BluetoothLe{
    fun startBluetoothLeScan()
    fun stopBluetoothLeScan()
    fun isScanning(): Flow<Boolean>
    fun getScanResults(): Flow<List<ScanResult>>

    fun connectToDeviceGatt(deviceAddress: String)
    fun disconnectFromGatt()

    fun getConnectionState(): Flow<Int>
    fun getDeviceServices(): Flow<List<BluetoothGattService>>
}