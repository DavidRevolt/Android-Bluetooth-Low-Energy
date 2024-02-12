package com.davidrevolt.core.data.repository

import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import com.davidrevolt.core.ble.BluetoothLe
import com.davidrevolt.core.data.modelmapper.asCustomGattService
import com.davidrevolt.core.data.modelmapper.asCustomScanResult
import com.davidrevolt.core.model.CustomGattService
import com.davidrevolt.core.model.CustomScanResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BluetoothLowEnergyRepositoryImpl @Inject constructor(private val ble: BluetoothLe) :
    BluetoothLowEnergyRepository {
    override fun startBluetoothLeScan() = ble.startBluetoothLeScan()

    override fun stopBluetoothLeScan() = ble.stopBluetoothLeScan()

    override fun isScanning(): Flow<Boolean> = ble.isScanning()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun getScanResults(): Flow<List<CustomScanResult>> =
        ble.getScanResults().map { list -> list.map(ScanResult::asCustomScanResult) }

    override fun connectToDeviceGatt(deviceAddress: String) =
        ble.connectToDeviceGatt(deviceAddress = deviceAddress)

    override fun disconnectFromGatt() = ble.disconnectFromGatt()

    override fun getConnectionState(): Flow<Int> = ble.getConnectionState()

    override fun getDeviceServices(): Flow<List<CustomGattService>> =
        ble.getDeviceServices().map { list ->
            list.map(
                BluetoothGattService::asCustomGattService
            )
        }

}