package com.davidrevolt.core.ble.model

import java.util.UUID

data class CustomGattCharacteristics(
    val uuid: UUID,
    val name: String,
    val properties: List<PropertiesAsEnum>, // Doesn't mean we have permission to use the prop
    val canRead: Boolean,
    val canWrite: Boolean,
    val readBytes: ByteArray?
)
