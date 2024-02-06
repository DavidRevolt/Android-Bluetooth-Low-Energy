package com.davidrevolt.core.ble.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/*
Online Guide: https://punchthrough.com/android-ble-guide/

* IMPORTANT: Make sure user have permission before using methods.
* IMPORTANT: Make sure user Device Bluetooth is enabled.
* IMPORTANT: You should always stop your BLE scan before connecting to a BLE device.

* ScanFilter - set the filtering criteria,
Easiest way to make sure an app only ever picks up devices running said custom firmware is to
generate a random UUID, and have the firmware advertise this UUID.

* Rssi - signal strength of the advertising BluetoothLe device, measured in dBm.
Sorting scan results by descending order of signal strength is a good way
to find the peripheral closest to the Android device

* Warning: a device implementing Bluetooth 4.2’s LE Privacy feature will
randomize its public MAC address periodically


* Scan settings:
Most apps that are scanning in the foreground should use SCAN_MODE_BALANCED [30sec scan].
SCAN_MODE_LOW_LATENCY is recommended if the app will only be scanning for a brief period of time,
typically to find a very specific type of device.
SCAN_MODE_LOW_POWER is used for extremely long-duration scans, or for scans that take place in the background
 */

class Scanner @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val _bluetoothManager: BluetoothManager? =
        ContextCompat.getSystemService(context, BluetoothManager::class.java)
    private val _bluetoothAdapter: BluetoothAdapter? = _bluetoothManager?.adapter

    private var _isScanning = false

    // Holds devices found through scanning.
    private val _scanResults = mutableListOf<ScanResult>()

    /*
    * onScanResult callback is flooded by ScanResults belonging to the same set of devices,
    * because signal strength (RSSI) readings constantly changing.
    */
    private val _scanCallback = object : ScanCallback() {
        @RequiresApi(Build.VERSION_CODES.S)
        @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_CONNECT])
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            // Check device is already in the result list
            val indexQuery = _scanResults.indexOfFirst { it.device.address == result.device.address }
            if(indexQuery==-1){
                _scanResults.add(result)
                Log.i("AppLog", "Found BLE device! Name: ${result.device.name }, address: ${result.device.address}")
            }
            else{
                _scanResults[indexQuery] = result
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("AppLog", "onScanFailed: code $errorCode")
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_SCAN])
    fun startBluetoothLeScan() {
        if (_bluetoothAdapter != null && _bluetoothAdapter.isEnabled) {
            val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()
            val bleScanner = _bluetoothAdapter.bluetoothLeScanner
            bleScanner.startScan(null, scanSettings, _scanCallback)
            _isScanning = true
        } else {
            Log.e("AppLog", "Device doesn't support Bluetooth or Bluetooth is disabled")
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_SCAN])
    fun stopBluetoothLeScan() {
        if (_bluetoothAdapter != null) {
            val bleScanner = _bluetoothAdapter.bluetoothLeScanner
            bleScanner.stopScan(_scanCallback)
            Log.i("AppLog", "Bluetooth scanning stopped")
            _isScanning = false
        }
    }

    fun isScanning() = _isScanning
}

