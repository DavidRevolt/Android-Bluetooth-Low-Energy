package com.davidrevolt.core.ble.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice.ADDRESS_TYPE_PUBLIC
import android.bluetooth.BluetoothDevice.TRANSPORT_LE
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


/*
Connect to a GATT server hosted by device., The caller (the Android app) is the GATT client
Online Guide: https://punchthrough.com/android-ble-guide/

* IMPORTANT: You should always stop your BLE scan before connecting to a BLE device.

 */


class BluetoothLeConnectService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _bluetoothManager: BluetoothManager? =
        ContextCompat.getSystemService(context, BluetoothManager::class.java)
    private val _bluetoothAdapter: BluetoothAdapter? = _bluetoothManager?.adapter

    private var _bluetoothGatt: BluetoothGatt? = null
    private val _connectionState = MutableStateFlow(BluetoothProfile.STATE_DISCONNECTED)
    private val _deviceServices = MutableStateFlow<List<BluetoothGattService>>(emptyList())
    fun getConnectionState() = _connectionState.asStateFlow()
    fun getDeviceServices() = _deviceServices.asStateFlow()


    private val gattCallback = object : BluetoothGattCallback() {
        @RequiresApi(Build.VERSION_CODES.S)
        @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT])
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            val deviceName = gatt.device.name ?: gatt.device.address

            if (status == BluetoothGatt.GATT_SUCCESS) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTING -> {
                        Log.i("AppLog", "Connecting to $deviceName")
                        _connectionState.value = BluetoothProfile.STATE_CONNECTING
                    }

                    BluetoothProfile.STATE_CONNECTED -> {
                        Log.i("AppLog", "Successfully connected to $deviceName")
                        _connectionState.value = BluetoothProfile.STATE_CONNECTED
                        _bluetoothGatt?.discoverServices()
                    }

                    BluetoothProfile.STATE_DISCONNECTING -> {
                        Log.i("AppLog", "disconnecting from $deviceName")
                        _connectionState.value = BluetoothProfile.STATE_DISCONNECTING
                    }

                    BluetoothProfile.STATE_DISCONNECTED -> {
                        Log.i("AppLog", "Disconnected from $deviceName")
                        _connectionState.value = BluetoothProfile.STATE_DISCONNECTED
                    }
                }
            } else {
                Log.e("AppLog", "Error $status encountered for $deviceName! Disconnecting...")
                _connectionState.value = BluetoothProfile.STATE_DISCONNECTED
                gatt.close()
            }
        }

        /*Find out device Services and Characteristics and convert them to MY Local models*/
        @RequiresApi(Build.VERSION_CODES.S)
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            _deviceServices.value = emptyList()
            val deviceName = gatt.device.name ?: gatt.device.address
            Log.i("AppLog", "Discovered ${gatt.services.size} services for $deviceName")

            _deviceServices.value = gatt.services
            gatt.services.forEach { service ->
                val characteristicsTable = service.characteristics.joinToString(
                    separator = "\n|--",
                    prefix = "|--"
                ) { it.uuid.toString() }
                Log.i(
                    "AppLog", "\nService ${service.uuid}\nCharacteristics:\n$characteristicsTable"
                )
            }
        }


        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {

            with(characteristic) {
                when (status) {
                    BluetoothGatt.GATT_SUCCESS -> {
                        Log.i("AppLog2", "Read characteristic $uuid:\n${value.toHexString()}")
                        Log.i("AppLog2", "Read characteristic ${value.toString(Charsets.UTF_8)}")
                    }

                    BluetoothGatt.GATT_READ_NOT_PERMITTED -> {
                        Log.e("AppLog2", "Read not permitted for $uuid!")
                    }

                    else -> {
                        Log.e("AppLog2", "Characteristic read failed for $uuid, error: $status")
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT])
    fun connectToDeviceGatt(deviceAddress: String) {
        _connectionState.value = BluetoothProfile.STATE_CONNECTING
       val device = _bluetoothAdapter?.getRemoteLeDevice(deviceAddress,ADDRESS_TYPE_PUBLIC)
        _bluetoothGatt = device?.connectGatt(context, false, gattCallback, TRANSPORT_LE)
    }

    //TODO: Check if need to delete log because callback as methods already
    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnectFromGatt() {
        _connectionState.value = BluetoothProfile.STATE_DISCONNECTING
        val deviceName = _bluetoothGatt?.device?.name ?: _bluetoothGatt?.device?.address
        Log.i("AppLog", "Disconnected from $deviceName")
        _bluetoothGatt?.disconnect()
        _bluetoothGatt?.close()
        _connectionState.value = BluetoothProfile.STATE_DISCONNECTED
        _deviceServices.value = emptyList()
        _bluetoothGatt = null
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT])
    fun readCharacteristic(gattCharacteristic: BluetoothGattCharacteristic) {
       _bluetoothGatt?.readCharacteristic(gattCharacteristic)
    }


    fun ByteArray.toHexString(): String =
        joinToString(separator = " ", prefix = "0x") { String.format("%02X", it) }

}