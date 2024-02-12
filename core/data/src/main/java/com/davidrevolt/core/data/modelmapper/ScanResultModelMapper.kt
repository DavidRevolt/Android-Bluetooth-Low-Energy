package com.davidrevolt.core.data.modelmapper

import android.Manifest
import android.bluetooth.le.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.davidrevolt.core.model.CustomScanResult

@RequiresApi(Build.VERSION_CODES.S)
@RequiresPermission( Manifest.permission.BLUETOOTH_CONNECT)
fun ScanResult.asCustomScanResult() =
    CustomScanResult(
        name = this.device.name?: "UnKnown",
        address = this.device.address,
        rssi = this.rssi
    )