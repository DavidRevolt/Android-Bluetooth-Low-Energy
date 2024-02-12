package com.davidrevolt.core.model

import java.util.UUID

data class CustomGattService(
    val uuid: UUID,
    val name: String,
    val characteristics: List<CustomGattCharacteristics>
)

