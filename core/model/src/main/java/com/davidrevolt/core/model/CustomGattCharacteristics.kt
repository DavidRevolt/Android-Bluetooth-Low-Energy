package com.davidrevolt.core.model

import java.util.UUID

data class CustomGattCharacteristics(
    val uuid: UUID,
    val name: String,
    val properties: List<PropertiesAsEnum>,
    val canRead: Boolean,
    val canWrite: Boolean,
    val readBytes: ByteArray?
)

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