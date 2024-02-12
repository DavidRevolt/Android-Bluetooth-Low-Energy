package com.davidrevolt.core.data.modelmapper

import android.bluetooth.BluetoothGattCharacteristic
import com.davidrevolt.core.data.utils.asName
import com.davidrevolt.core.model.PropertiesAsEnum


/*
* Convert BluetoothGattCharacteristic to CustomGattCharacteristics which is more readable Characteristic obj
*/
fun BluetoothGattCharacteristic.asCustomDeviceCharacteristics() =
    com.davidrevolt.core.model.CustomGattCharacteristics(
        uuid = this.uuid,
        name = this.uuid.asName(),
        properties = this.propertiesAsList(),
        canRead = this.containsProperty(BluetoothGattCharacteristic.PROPERTY_READ),
        canWrite = this.containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE),
        readBytes = null
    )



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
    com.davidrevolt.core.model.PropertiesAsEnum.entries.forEach { propertiesAsEnum ->
        if(this.containsProperty(propertiesAsEnum.value))
            propertyList.add(propertiesAsEnum)
    }
    return propertyList
}
