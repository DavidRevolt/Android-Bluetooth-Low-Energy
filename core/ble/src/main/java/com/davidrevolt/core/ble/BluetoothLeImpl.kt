package com.davidrevolt.core.ble

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.davidrevolt.core.ble.model.CustomGattService
import com.davidrevolt.core.ble.model.CustomScanResult
import com.davidrevolt.core.ble.manger.BluetoothLeConnect
import com.davidrevolt.core.ble.manger.BluetoothLeScan
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject


class BluetoothLeImpl @Inject constructor(
    private val bluetoothLeScan: BluetoothLeScan,
    private val bluetoothLeConnect: BluetoothLeConnect
) :
    BluetoothLe {

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN])
    override fun startBluetoothLeScan() =
        bluetoothLeScan.startBluetoothLeScan()


    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN])
    override fun stopBluetoothLeScan() = bluetoothLeScan.stopBluetoothLeScan()

    override fun isScanning(): Flow<Boolean> = bluetoothLeScan.isScanning()
    override fun getScanResults(): Flow<List<CustomScanResult>> =
        bluetoothLeScan.getScanResults()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])

    override fun connectToDeviceGatt(deviceAddress: String) {
        // Always stop BLE scan before connecting to a BLE device.
        bluetoothLeScan.stopBluetoothLeScan()
        bluetoothLeConnect.connectToDeviceGatt(deviceAddress = deviceAddress)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun disconnectFromGatt() =
        bluetoothLeConnect.disconnectFromGatt()

    override fun getConnectionState(): Flow<Int> =
        bluetoothLeConnect.getConnectionState()

    override fun getDeviceServices(): Flow<List<CustomGattService>> =
        bluetoothLeConnect.getDeviceServices()

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT])
    override fun readCharacteristic(characteristicUUID: UUID) =
        bluetoothLeConnect.readCharacteristic(characteristicUUID = characteristicUUID)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT])
    override fun writeCharacteristic(
        characteristicUUID: UUID,
        value: ByteArray
    ) = bluetoothLeConnect.writeCharacteristic(
        characteristicUUID = characteristicUUID,
        value = value
    )

}