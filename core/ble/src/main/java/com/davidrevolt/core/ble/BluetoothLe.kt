package com.davidrevolt.core.ble

interface BluetoothLe{
    fun startBluetoothLeScan()
    fun stopBluetoothLeScan()
    fun isScanning():Boolean
}