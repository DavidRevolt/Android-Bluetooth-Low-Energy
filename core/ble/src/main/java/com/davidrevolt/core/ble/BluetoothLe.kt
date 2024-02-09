package com.davidrevolt.core.ble

import android.bluetooth.le.ScanResult
import kotlinx.coroutines.flow.Flow

interface BluetoothLe{
    fun startBluetoothLeScan()
    fun stopBluetoothLeScan()
    fun isScanning(): Flow<Boolean>
    fun getScanResults(): Flow<List<ScanResult>>

}