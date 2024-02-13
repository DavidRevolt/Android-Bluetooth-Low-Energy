package com.davidrevolt.core.ble.model

import java.util.UUID

data class CustomGattService(
    val uuid: UUID,
    val name: String,
    val characteristics: List<CustomGattCharacteristics>
)

