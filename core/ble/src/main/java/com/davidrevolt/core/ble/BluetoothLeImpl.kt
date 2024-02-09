package com.davidrevolt.core.ble

import android.Manifest
import android.bluetooth.le.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.davidrevolt.core.ble.util.BluetoothLeScanner
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class BluetoothLeImpl @Inject constructor(private val bluetoothLeScanner: BluetoothLeScanner) :
    BluetoothLe {

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_SCAN])
    override fun startBluetoothLeScan() =
        bluetoothLeScanner.startBluetoothLeScan()


    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_SCAN])
    override fun stopBluetoothLeScan() = bluetoothLeScanner.stopBluetoothLeScan()

    override fun isScanning(): Flow<Boolean> = bluetoothLeScanner.isScanning()
    override fun getScanResults(): Flow<List<ScanResult>> = bluetoothLeScanner.getScanResults()

}