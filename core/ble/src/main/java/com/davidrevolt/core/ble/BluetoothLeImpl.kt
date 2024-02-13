package com.davidrevolt.core.ble

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.davidrevolt.core.ble.model.CustomGattService
import com.davidrevolt.core.ble.model.CustomScanResult
import com.davidrevolt.core.ble.util.BluetoothLeConnectService
import com.davidrevolt.core.ble.util.BluetoothLeScanService
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject


class BluetoothLeImpl @Inject constructor(
    private val bluetoothLeScanService: BluetoothLeScanService,
    private val bluetoothLeConnectService: BluetoothLeConnectService
) :
    BluetoothLe {

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN])
    override fun startBluetoothLeScan() =
        bluetoothLeScanService.startBluetoothLeScan()


    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN])
    override fun stopBluetoothLeScan() = bluetoothLeScanService.stopBluetoothLeScan()

    override fun isScanning(): Flow<Boolean> = bluetoothLeScanService.isScanning()
    override fun getScanResults(): Flow<List<CustomScanResult>> = bluetoothLeScanService.getScanResults()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])

    override fun connectToDeviceGatt(deviceAddress: String) {
        // Always stop BLE scan before connecting to a BLE device.
        bluetoothLeScanService.stopBluetoothLeScan()
        bluetoothLeConnectService.connectToDeviceGatt(deviceAddress = deviceAddress)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun disconnectFromGatt() =
        bluetoothLeConnectService.disconnectFromGatt()

    override fun getConnectionState(): Flow<Int> =
        bluetoothLeConnectService.getConnectionState()

    override fun getDeviceServices(): Flow<List<CustomGattService>> =
        bluetoothLeConnectService.getDeviceServices()

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT])
    override fun readCharacteristic(characteristicUUID: UUID, serviceUUID: UUID) =
        bluetoothLeConnectService.readCharacteristic(
            characteristicUUID = characteristicUUID,
            serviceUUID = serviceUUID
        )

}