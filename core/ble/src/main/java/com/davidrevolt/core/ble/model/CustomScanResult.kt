package com.davidrevolt.core.ble.model

data class CustomScanResult(
    val name: String?,
    val address: String,
    val rssi: Int,
    val manufacturer: String?,
)