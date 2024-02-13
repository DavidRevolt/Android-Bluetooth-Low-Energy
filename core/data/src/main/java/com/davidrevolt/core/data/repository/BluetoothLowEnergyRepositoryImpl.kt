package com.davidrevolt.core.data.repository

import com.davidrevolt.core.ble.BluetoothLe
import javax.inject.Inject

class BluetoothLowEnergyRepositoryImpl @Inject constructor(private val ble: BluetoothLe) :
    BluetoothLowEnergyRepository {
}