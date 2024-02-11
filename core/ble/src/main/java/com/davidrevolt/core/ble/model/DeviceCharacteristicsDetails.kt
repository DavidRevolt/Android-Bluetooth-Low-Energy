package com.davidrevolt.core.ble.model

import java.util.UUID

data class DeviceCharacteristicsDetails(
    val uuid: UUID,
    val name: String,
    val canRead: Boolean,
    val canWrite: Boolean,
    val readBytes: ByteArray?,
)
