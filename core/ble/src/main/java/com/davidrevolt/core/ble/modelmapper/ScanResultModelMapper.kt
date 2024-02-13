package com.davidrevolt.core.ble.modelmapper

import android.Manifest
import android.bluetooth.le.ScanResult
import android.os.Build
import android.util.SparseArray
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.davidrevolt.core.ble.model.CustomScanResult

@RequiresApi(Build.VERSION_CODES.S)
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun ScanResult.asCustomScanResult() =
    CustomScanResult(
        name = this.device.name ?: "UnKnown",
        address = this.device.address,
        rssi = this.rssi,
        manufacturer = getManufacturerName(this.scanRecord?.manufacturerSpecificData)
    )



//TODO: ManufacturerId to name method
// TODO: Move this to other file
fun getManufacturerName(manufacturerSpecificData: SparseArray<ByteArray>?): String? {
    val manufacturerId = getManufacturerId(manufacturerSpecificData)
    val name = manufacturerId?.let {
        manufacturerId.toString()
    }
    return name
}

fun getManufacturerId(manufacturerSpecificData: SparseArray<ByteArray>?): Int? {
    var mfId: Int? = null
    manufacturerSpecificData?.let {
        for (i in 0 until manufacturerSpecificData.size()) {
            mfId = manufacturerSpecificData.keyAt(i)
        }
    }
    return mfId
}


