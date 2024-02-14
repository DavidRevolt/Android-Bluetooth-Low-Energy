package com.davidrevolt.core.ble.model


enum class PropertiesAsEnum(val value: Int) {
    PROPERTY_BROADCAST(1),
    PROPERTY_EXTENDED_PROPS(128),
    PROPERTY_INDICATE(32),
    PROPERTY_NOTIFY(16),
    PROPERTY_READ(2),
    PROPERTY_SIGNED_WRITE(64),
    PROPERTY_WRITE(8),
    PROPERTY_WRITE_NO_RESPONSE(4);
}