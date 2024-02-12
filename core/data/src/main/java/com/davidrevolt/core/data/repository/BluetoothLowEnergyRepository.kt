package com.davidrevolt.core.data.repository

import com.davidrevolt.core.model.CustomGattService
import com.davidrevolt.core.model.CustomScanResult
import kotlinx.coroutines.flow.Flow

interface BluetoothLowEnergyRepository {
    fun startBluetoothLeScan()
    fun stopBluetoothLeScan()
    fun isScanning(): Flow<Boolean>
    fun getScanResults(): Flow<List<CustomScanResult>>

    fun connectToDeviceGatt(deviceAddress: String)
    fun disconnectFromGatt()

    fun getConnectionState(): Flow<Int>
    fun getDeviceServices(): Flow<List<CustomGattService>>
}