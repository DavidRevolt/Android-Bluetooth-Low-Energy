package com.davidrevolt.core.ble

import com.davidrevolt.core.ble.model.CustomGattService
import com.davidrevolt.core.ble.model.CustomScanResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface BluetoothLe{
    fun startBluetoothLeScan()
    fun stopBluetoothLeScan()
    fun isScanning(): Flow<Boolean>
    fun getScanResults(): Flow<List<CustomScanResult>>

    fun connectToDeviceGatt(deviceAddress: String)
    fun disconnectFromGatt()

    fun getConnectionState(): Flow<Int>
    fun getDeviceServices(): Flow<List<CustomGattService>>
    fun readCharacteristic(characteristicUUID: UUID)
    fun writeCharacteristic(characteristicUUID: UUID, value: ByteArray)
}