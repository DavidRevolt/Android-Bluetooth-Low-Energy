package com.davidrevolt.core.ble

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.davidrevolt.core.ble.util.Scanner
import javax.inject.Inject


class BluetoothLeImpl @Inject constructor(private val scanner: Scanner) :
    BluetoothLe {

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_SCAN])
    override fun startBluetoothLeScan() =
        scanner.startBluetoothLeScan()


    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_SCAN])
    override fun stopBluetoothLeScan() = scanner.stopBluetoothLeScan()

    override fun isScanning(): Boolean = scanner.isScanning()

}