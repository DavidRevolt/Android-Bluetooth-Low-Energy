package com.davidrevolt.core.ble.modelmapper

import android.bluetooth.BluetoothGattCharacteristic
import com.davidrevolt.core.ble.model.CustomGattCharacteristics
import com.davidrevolt.core.ble.model.PropertiesAsEnum
import java.util.UUID


/*
* Convert BluetoothGattCharacteristic to CustomGattCharacteristics which is more readable Characteristic obj
*/
fun BluetoothGattCharacteristic.asCustomDeviceCharacteristics() =
    CustomGattCharacteristics(
        uuid = this.uuid,
        name = this.uuid.asName(),
        properties = this.propertiesAsList(),
        canRead = this.containsProperty(BluetoothGattCharacteristic.PROPERTY_READ),
        canWrite = this.containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE),
        readBytes = null
    )


//TODO: Impl method
// TODO: Move this to other file
fun UUID.asName(): String {
    return "UnKnown"
}


/*
* To know if Characteristic support one of this properties [support doesn't mean we have permission to]
* We performer AND operation with Characteristic.properties value AND the property we want:
    PROPERTY_BROADCAST(1),
    PROPERTY_EXTENDED_PROPS(128),
    PROPERTY_INDICATE(32),
    PROPERTY_NOTIFY(16),
    PROPERTY_READ(2),
    PROPERTY_SIGNED_WRITE(64),
    PROPERTY_WRITE(8),
    PROPERTY_WRITE_NO_RESPONSE(4)

 */
fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean {
    return properties and property != 0
}


/*
* Convert Characteristic.properties value to list of all the supported properties as enum
*/
fun BluetoothGattCharacteristic.propertiesAsList(): List<PropertiesAsEnum> {
    val propertyList = mutableListOf<PropertiesAsEnum>()
    PropertiesAsEnum.entries.forEach { propertiesAsEnum ->
        if (this.containsProperty(propertiesAsEnum.value))
            propertyList.add(propertiesAsEnum)
    }
    return propertyList
}
