package com.davidrevolt.core.ble.modelmapper

import android.bluetooth.BluetoothGattCharacteristic
import com.davidrevolt.core.ble.model.CustomGattCharacteristics
import com.davidrevolt.core.ble.util.GattCharacteristic.containsProperty
import com.davidrevolt.core.ble.util.GattCharacteristic.propertiesAsList
import com.davidrevolt.core.ble.util.GattCharacteristic.toName


/*
* Convert BluetoothGattCharacteristic to CustomGattCharacteristics which is more readable Characteristic obj
*/
fun BluetoothGattCharacteristic.asCustomDeviceCharacteristics() =
    CustomGattCharacteristics(
        uuid = this.uuid,
        name = this.toName(),
        properties = this.propertiesAsList(),
        canRead = this.containsProperty(BluetoothGattCharacteristic.PROPERTY_READ),
        canWrite = this.containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE) ||
                this.containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE),
        readBytes = null
    )


