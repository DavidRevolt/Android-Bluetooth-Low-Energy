package com.davidrevolt.core.ble

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.davidrevolt.core.ble.util.BluetoothLeConnectService
import com.davidrevolt.core.ble.util.BluetoothLeScanService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class BluetoothLeImpl @Inject constructor(private val bluetoothLeScanService: BluetoothLeScanService, private val bluetoothLeConnectService: BluetoothLeConnectService) :
    BluetoothLe {

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf  = [Manifest.permission.BLUETOOTH_SCAN])
    override fun startBluetoothLeScan() =
        bluetoothLeScanService.startBluetoothLeScan()


    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf  = [Manifest.permission.BLUETOOTH_SCAN])
    override fun stopBluetoothLeScan() = bluetoothLeScanService.stopBluetoothLeScan()

    override fun isScanning(): Flow<Boolean> = bluetoothLeScanService.isScanning()
    override fun getScanResults(): Flow<List<ScanResult>> = bluetoothLeScanService.getScanResults()

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf  = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])

    override fun connectToDeviceGatt(device: BluetoothDevice){
        // Always stop BLE scan before connecting to a BLE device.
        bluetoothLeScanService.stopBluetoothLeScan()
        bluetoothLeConnectService.connectToDeviceGatt(device = device)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission( Manifest.permission.BLUETOOTH_CONNECT)
    override fun disconnectFromGatt(bluetoothGatt: BluetoothGatt) =
        bluetoothLeConnectService.disconnectFromGatt(bluetoothGatt = bluetoothGatt)


}