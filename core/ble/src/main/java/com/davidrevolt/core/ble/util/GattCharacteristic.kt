package com.davidrevolt.core.ble.util

import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import com.davidrevolt.core.ble.model.PropertiesAsEnum
import java.lang.reflect.Modifier
import java.util.Locale
import java.util.UUID

const val BASE_UUID = "0000%s-0000-1000-8000-00805f9b34fb"
object GattCharacteristic {
    private var uuidToName: MutableMap<UUID, String>? = null

    /**
     * Convert The Characteristic.UUID TO Readable Name
     * */
    fun BluetoothGattCharacteristic.toName( ): String {
        if(uuidToName ==null)
            initUuidToNameMap()
        val name = uuidToName?.get(uuid) ?: "Unknown Characteristic"
        return this.uuid.toString() + " (" + name + ")"
    }

    fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean {
        return properties and property != 0
    }

    /**
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



    private fun toPrettyName(fieldName: String): String {
        val words = fieldName.split("_".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (words.size <= 1) {
            return fieldName.lowercase(Locale.getDefault())
        }
        val builder = StringBuilder(fieldName.length)
        for (word in words) {
            if (word.isEmpty() || "UUID" == word || "CHARACTERISTIC" == word) {
                continue
            }
            if (builder.isNotEmpty()) {
                builder.append(" ")
            }
            builder.append(word.lowercase(Locale.getDefault()))
        }
        return builder.toString()
    }






    private fun initUuidToNameMap() {
        try {
            uuidToName = hashMapOf()
            for (field in GattCharacteristic::class.java.declaredFields) {
                if (field.modifiers and Modifier.STATIC !== 0 && field.type === UUID::class.java) {
                    val uuid = field[null] as UUID
                    uuidToName!![uuid] = toPrettyName(field.name)
                }
            }
        } catch (ex: Exception) {
            Log.e(
                "GattCharacteristicInit",
                "Error reading GattCharacteristicUUID fields by reflection: " + ex.message,
                ex
            )
        }
    }
    //part of the generic BLE specs see https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicsHome.aspx
    // this list of GATT characteristics is complete as of 2020-01-18, retrieved from https://btprodspecificationrefs.blob.core.windows.net/assigned-values/16-bit%20UUID%20Numbers%20Document.pdf
    val UUID_CHARACTERISTIC_DEVICE_NAME =
        UUID.fromString(String.format(BASE_UUID, "2A00"))
    val UUID_CHARACTERISTIC_APPEARANCE =
        UUID.fromString(String.format(BASE_UUID, "2A01"))
    val UUID_CHARACTERISTIC_PERIPHERAL_PRIVACY_FLAG =
        UUID.fromString(String.format(BASE_UUID, "2A02"))
    val UUID_CHARACTERISTIC_RECONNECTION_ADDRESS =
        UUID.fromString(String.format(BASE_UUID, "2A03"))
    val UUID_CHARACTERISTIC_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = UUID.fromString(
        String.format(BASE_UUID, "2A04")
    )
    val UUID_CHARACTERISTIC_SERVICE_CHANGED =
        UUID.fromString(String.format(BASE_UUID, "2A05"))
    val UUID_CHARACTERISTIC_ALERT_LEVEL =
        UUID.fromString(String.format(BASE_UUID, "2A06"))
    val UUID_CHARACTERISTIC_TX_POWER_LEVEL =
        UUID.fromString(String.format(BASE_UUID, "2A07"))
    val UUID_CHARACTERISTIC_DATE_TIME =
        UUID.fromString(String.format(BASE_UUID, "2A08"))
    val UUID_CHARACTERISTIC_DAY_OF_WEEK =
        UUID.fromString(String.format(BASE_UUID, "2A09"))
    val UUID_CHARACTERISTIC_DAY_DATE_TIME =
        UUID.fromString(String.format(BASE_UUID, "2A0A"))
    val UUID_CHARACTERISTIC_EXACT_TIME_256 =
        UUID.fromString(String.format(BASE_UUID, "2A0C"))
    val UUID_CHARACTERISTIC_DST_OFFSET =
        UUID.fromString(String.format(BASE_UUID, "2A0D"))
    val UUID_CHARACTERISTIC_TIME_ZONE =
        UUID.fromString(String.format(BASE_UUID, "2A0E"))
    val UUID_CHARACTERISTIC_LOCAL_TIME =
        UUID.fromString(String.format(BASE_UUID, "2A0F"))
    val UUID_CHARACTERISTIC_TIME_WITH_DST =
        UUID.fromString(String.format(BASE_UUID, "2A11"))
    val UUID_CHARACTERISTIC_TIME_ACCURACY =
        UUID.fromString(String.format(BASE_UUID, "2A12"))
    val UUID_CHARACTERISTIC_TIME_SOURCE =
        UUID.fromString(String.format(BASE_UUID, "2A13"))
    val UUID_CHARACTERISTIC_REFERENCE_TIME_INFORMATION =
        UUID.fromString(String.format(BASE_UUID, "2A14"))
    val UUID_CHARACTERISTIC_TIME_UPDATE_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A16"))
    val UUID_CHARACTERISTIC_TIME_UPDATE_STATE =
        UUID.fromString(String.format(BASE_UUID, "2A17"))
    val UUID_CHARACTERISTIC_GLUCOSE_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A18"))
    val UUID_CHARACTERISTIC_BATTERY_LEVEL =
        UUID.fromString(String.format(BASE_UUID, "2A19"))
    val UUID_CHARACTERISTIC_TEMPERATURE_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A1C"))
    val UUID_CHARACTERISTIC_TEMPERATURE_TYPE =
        UUID.fromString(String.format(BASE_UUID, "2A1D"))
    val UUID_CHARACTERISTIC_INTERMEDIATE_TEMPERATURE =
        UUID.fromString(String.format(BASE_UUID, "2A1E"))
    val UUID_CHARACTERISTIC_MEASUREMENT_INTERVAL =
        UUID.fromString(String.format(BASE_UUID, "2A21"))
    val UUID_CHARACTERISTIC_BOOT_KEYBOARD_INPUT_REPORT =
        UUID.fromString(String.format(BASE_UUID, "2A22"))
    val UUID_CHARACTERISTIC_SYSTEM_ID =
        UUID.fromString(String.format(BASE_UUID, "2A23"))
    val UUID_CHARACTERISTIC_MODEL_NUMBER_STRING =
        UUID.fromString(String.format(BASE_UUID, "2A24"))
    val UUID_CHARACTERISTIC_SERIAL_NUMBER_STRING =
        UUID.fromString(String.format(BASE_UUID, "2A25"))
    val UUID_CHARACTERISTIC_FIRMWARE_REVISION_STRING =
        UUID.fromString(String.format(BASE_UUID, "2A26"))
    val UUID_CHARACTERISTIC_HARDWARE_REVISION_STRING =
        UUID.fromString(String.format(BASE_UUID, "2A27"))
    val UUID_CHARACTERISTIC_SOFTWARE_REVISION_STRING =
        UUID.fromString(String.format(BASE_UUID, "2A28"))
    val UUID_CHARACTERISTIC_MANUFACTURER_NAME_STRING =
        UUID.fromString(String.format(BASE_UUID, "2A29"))
    val UUID_CHARACTERISTIC_IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST = UUID.fromString(
        String.format(BASE_UUID, "2A2A")
    )
    val UUID_CHARACTERISTIC_CURRENT_TIME =
        UUID.fromString(String.format(BASE_UUID, "2A2B"))
    val UUID_CHARACTERISTIC_SCAN_REFRESH =
        UUID.fromString(String.format(BASE_UUID, "2A31"))
    val UUID_CHARACTERISTIC_BOOT_KEYBOARD_OUTPUT_REPORT =
        UUID.fromString(String.format(BASE_UUID, "2A32"))
    val UUID_CHARACTERISTIC_BOOT_MOUSE_INPUT_REPORT =
        UUID.fromString(String.format(BASE_UUID, "2A33"))
    val UUID_CHARACTERISTIC_GLUCOSE_MEASUREMENT_CONTEXT =
        UUID.fromString(String.format(BASE_UUID, "2A34"))
    val UUID_CHARACTERISTIC_BLOOD_PRESSURE_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A35"))
    val UUID_CHARACTERISTIC_INTERMEDIATE_CUFF_PRESSURE =
        UUID.fromString(String.format(BASE_UUID, "2A36"))
    val UUID_CHARACTERISTIC_HEART_RATE_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A37"))
    val UUID_CHARACTERISTIC_BODY_SENSOR_LOCATION =
        UUID.fromString(String.format(BASE_UUID, "2A38"))
    val UUID_CHARACTERISTIC_HEART_RATE_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A39"))
    val UUID_CHARACTERISTIC_ALERT_STATUS =
        UUID.fromString(String.format(BASE_UUID, "2A3F"))
    val UUID_CHARACTERISTIC_RINGER_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A40"))
    val UUID_CHARACTERISTIC_RINGER_SETTING =
        UUID.fromString(String.format(BASE_UUID, "2A41"))
    val UUID_CHARACTERISTIC_ALERT_CATEGORY_ID_BIT_MASK =
        UUID.fromString(String.format(BASE_UUID, "2A42"))
    val UUID_CHARACTERISTIC_ALERT_CATEGORY_ID =
        UUID.fromString(String.format(BASE_UUID, "2A43"))
    val UUID_CHARACTERISTIC_ALERT_NOTIFICATION_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A44"))
    val UUID_CHARACTERISTIC_UNREAD_ALERT_STATUS =
        UUID.fromString(String.format(BASE_UUID, "2A45"))
    val UUID_CHARACTERISTIC_NEW_ALERT =
        UUID.fromString(String.format(BASE_UUID, "2A46"))
    val UUID_CHARACTERISTIC_SUPPORTED_NEW_ALERT_CATEGORY =
        UUID.fromString(String.format(BASE_UUID, "2A47"))
    val UUID_CHARACTERISTIC_SUPPORTED_UNREAD_ALERT_CATEGORY =
        UUID.fromString(String.format(BASE_UUID, "2A48"))
    val UUID_CHARACTERISTIC_BLOOD_PRESSURE_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2A49"))
    val UUID_CHARACTERISTIC_HID_INFORMATION =
        UUID.fromString(String.format(BASE_UUID, "2A4A"))
    val UUID_CHARACTERISTIC_REPORT_MAP =
        UUID.fromString(String.format(BASE_UUID, "2A4B"))
    val UUID_CHARACTERISTIC_HID_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A4C"))
    val UUID_CHARACTERISTIC_REPORT =
        UUID.fromString(String.format(BASE_UUID, "2A4D"))
    val UUID_CHARACTERISTIC_PROTOCOL_MODE =
        UUID.fromString(String.format(BASE_UUID, "2A4E"))
    val UUID_CHARACTERISTIC_SCAN_INTERVAL_WINDOW =
        UUID.fromString(String.format(BASE_UUID, "2A4F"))
    val UUID_CHARACTERISTIC_PNP_ID =
        UUID.fromString(String.format(BASE_UUID, "2A50"))
    val UUID_CHARACTERISTIC_GLUCOSE_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2A51"))
    val UUID_CHARACTERISTIC_RECORD_ACCESS_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A52"))
    val UUID_CHARACTERISTIC_RSC_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A53"))
    val UUID_CHARACTERISTIC_RSC_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2A54"))
    val UUID_CHARACTERISTIC_SC_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A55"))
    val UUID_CHARACTERISTIC_AGGREGATE =
        UUID.fromString(String.format(BASE_UUID, "2A5A"))
    val UUID_CHARACTERISTIC_CSC_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A5B"))
    val UUID_CHARACTERISTIC_CSC_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2A5C"))
    val UUID_CHARACTERISTIC_SENSOR_LOCATION =
        UUID.fromString(String.format(BASE_UUID, "2A5D"))
    val UUID_CHARACTERISTIC_PLX_SPOT_CHECK_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A5E"))
    val UUID_CHARACTERISTIC_PLX_CONTINUOUS_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A5F"))
    val UUID_CHARACTERISTIC_PLX_FEATURES =
        UUID.fromString(String.format(BASE_UUID, "2A60"))
    val UUID_CHARACTERISTIC_CYCLING_POWER_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A63"))
    val UUID_CHARACTERISTIC_CYCLING_POWER_VECTOR =
        UUID.fromString(String.format(BASE_UUID, "2A64"))
    val UUID_CHARACTERISTIC_CYCLING_POWER_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2A65"))
    val UUID_CHARACTERISTIC_CYCLING_POWER_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A66"))
    val UUID_CHARACTERISTIC_LOCATION_AND_SPEED =
        UUID.fromString(String.format(BASE_UUID, "2A67"))
    val UUID_CHARACTERISTIC_NAVIGATION =
        UUID.fromString(String.format(BASE_UUID, "2A68"))
    val UUID_CHARACTERISTIC_POSITION_QUALITY =
        UUID.fromString(String.format(BASE_UUID, "2A69"))
    val UUID_CHARACTERISTIC_LN_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2A6A"))
    val UUID_CHARACTERISTIC_LN_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A6B"))
    val UUID_CHARACTERISTIC_ELEVATION =
        UUID.fromString(String.format(BASE_UUID, "2A6C"))
    val UUID_CHARACTERISTIC_PRESSURE =
        UUID.fromString(String.format(BASE_UUID, "2A6D"))
    val UUID_CHARACTERISTIC_TEMPERATURE =
        UUID.fromString(String.format(BASE_UUID, "2A6E"))
    val UUID_CHARACTERISTIC_HUMIDITY =
        UUID.fromString(String.format(BASE_UUID, "2A6F"))
    val UUID_CHARACTERISTIC_TRUE_WIND_SPEED =
        UUID.fromString(String.format(BASE_UUID, "2A70"))
    val UUID_CHARACTERISTIC_TRUE_WIND_DIRECTION =
        UUID.fromString(String.format(BASE_UUID, "2A71"))
    val UUID_CHARACTERISTIC_APPARENT_WIND_SPEED =
        UUID.fromString(String.format(BASE_UUID, "2A72"))
    val UUID_CHARACTERISTIC_APPARENT_WIND_DIRECTION =
        UUID.fromString(String.format(BASE_UUID, "2A73"))
    val UUID_CHARACTERISTIC_GUST_FACTOR =
        UUID.fromString(String.format(BASE_UUID, "2A74"))
    val UUID_CHARACTERISTIC_POLLEN_CONCENTRATION =
        UUID.fromString(String.format(BASE_UUID, "2A75"))
    val UUID_CHARACTERISTIC_UV_INDEX =
        UUID.fromString(String.format(BASE_UUID, "2A76"))
    val UUID_CHARACTERISTIC_IRRADIANCE =
        UUID.fromString(String.format(BASE_UUID, "2A77"))
    val UUID_CHARACTERISTIC_RAINFALL =
        UUID.fromString(String.format(BASE_UUID, "2A78"))
    val UUID_CHARACTERISTIC_WIND_CHILL =
        UUID.fromString(String.format(BASE_UUID, "2A79"))
    val UUID_CHARACTERISTIC_HEAT_INDEX =
        UUID.fromString(String.format(BASE_UUID, "2A7A"))
    val UUID_CHARACTERISTIC_DEW_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A7B"))
    val UUID_CHARACTERISTIC_DESCRIPTOR_VALUE_CHANGED =
        UUID.fromString(String.format(BASE_UUID, "2A7D"))
    val UUID_CHARACTERISTIC_AEROBIC_HEART_RATE_LOWER_LIMIT =
        UUID.fromString(String.format(BASE_UUID, "2A7E"))
    val UUID_CHARACTERISTIC_AEROBIC_THRESHOLD =
        UUID.fromString(String.format(BASE_UUID, "2A7F"))
    val UUID_CHARACTERISTIC_AGE =
        UUID.fromString(String.format(BASE_UUID, "2A80"))
    val UUID_CHARACTERISTIC_ANAEROBIC_HEART_RATE_LOWER_LIMIT =
        UUID.fromString(String.format(BASE_UUID, "2A81"))
    val UUID_CHARACTERISTIC_ANAEROBIC_HEART_RATE_UPPER_LIMIT =
        UUID.fromString(String.format(BASE_UUID, "2A82"))
    val UUID_CHARACTERISTIC_ANAEROBIC_THRESHOLD =
        UUID.fromString(String.format(BASE_UUID, "2A83"))
    val UUID_CHARACTERISTIC_AEROBIC_HEART_RATE_UPPER_LIMIT =
        UUID.fromString(String.format(BASE_UUID, "2A84"))
    val UUID_CHARACTERISTIC_DATE_OF_BIRTH =
        UUID.fromString(String.format(BASE_UUID, "2A85"))
    val UUID_CHARACTERISTIC_DATE_OF_THRESHOLD_ASSESSMENT =
        UUID.fromString(String.format(BASE_UUID, "2A86"))
    val UUID_CHARACTERISTIC_EMAIL_ADDRESS =
        UUID.fromString(String.format(BASE_UUID, "2A87"))
    val UUID_CHARACTERISTIC_FAT_BURN_HEART_RATE_LOWER_LIMIT =
        UUID.fromString(String.format(BASE_UUID, "2A88"))
    val UUID_CHARACTERISTIC_FAT_BURN_HEART_RATE_UPPER_LIMIT =
        UUID.fromString(String.format(BASE_UUID, "2A89"))
    val UUID_CHARACTERISTIC_FIRST_NAME =
        UUID.fromString(String.format(BASE_UUID, "2A8A"))
    val UUID_CHARACTERISTIC_FIVE_ZONE_HEART_RATE_LIMITS =
        UUID.fromString(String.format(BASE_UUID, "2A8B"))
    val UUID_CHARACTERISTIC_GENDER =
        UUID.fromString(String.format(BASE_UUID, "2A8C"))
    val UUID_CHARACTERISTIC_HEART_RATE_MAX =
        UUID.fromString(String.format(BASE_UUID, "2A8D"))
    val UUID_CHARACTERISTIC_HEIGHT =
        UUID.fromString(String.format(BASE_UUID, "2A8E"))
    val UUID_CHARACTERISTIC_HIP_CIRCUMFERENCE =
        UUID.fromString(String.format(BASE_UUID, "2A8F"))
    val UUID_CHARACTERISTIC_LAST_NAME =
        UUID.fromString(String.format(BASE_UUID, "2A90"))
    val UUID_CHARACTERISTIC_MAXIMUM_RECOMMENDED_HEART_RATE =
        UUID.fromString(String.format(BASE_UUID, "2A91"))
    val UUID_CHARACTERISTIC_RESTING_HEART_RATE =
        UUID.fromString(String.format(BASE_UUID, "2A92"))
    val UUID_CHARACTERISTIC_SPORT_TYPE_FOR_AEROBIC_AND_ANAEROBIC_THRESHOLDS = UUID.fromString(
        String.format(BASE_UUID, "2A93")
    )
    val UUID_CHARACTERISTIC_THREE_ZONE_HEART_RATE_LIMITS =
        UUID.fromString(String.format(BASE_UUID, "2A94"))
    val UUID_CHARACTERISTIC_TWO_ZONE_HEART_RATE_LIMITS =
        UUID.fromString(String.format(BASE_UUID, "2A95"))
    val UUID_CHARACTERISTIC_VO2_MAX =
        UUID.fromString(String.format(BASE_UUID, "2A96"))
    val UUID_CHARACTERISTIC_WAIST_CIRCUMFERENCE =
        UUID.fromString(String.format(BASE_UUID, "2A97"))
    val UUID_CHARACTERISTIC_WEIGHT =
        UUID.fromString(String.format(BASE_UUID, "2A98"))
    val UUID_CHARACTERISTIC_DATABASE_CHANGE_INCREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A99"))
    val UUID_CHARACTERISTIC_USER_INDEX =
        UUID.fromString(String.format(BASE_UUID, "2A9A"))
    val UUID_CHARACTERISTIC_BODY_COMPOSITION_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2A9B"))
    val UUID_CHARACTERISTIC_BODY_COMPOSITION_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A9C"))
    val UUID_CHARACTERISTIC_WEIGHT_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2A9D"))
    val UUID_CHARACTERISTIC_WEIGHT_SCALE_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2A9E"))
    val UUID_CHARACTERISTIC_USER_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2A9F"))
    val UUID_CHARACTERISTIC_MAGNETIC_FLUX_DENSITY___2D =
        UUID.fromString(String.format(BASE_UUID, "2AA0"))
    val UUID_CHARACTERISTIC_MAGNETIC_FLUX_DENSITY___3D =
        UUID.fromString(String.format(BASE_UUID, "2AA1"))
    val UUID_CHARACTERISTIC_LANGUAGE =
        UUID.fromString(String.format(BASE_UUID, "2AA2"))
    val UUID_CHARACTERISTIC_BAROMETRIC_PRESSURE_TREND =
        UUID.fromString(String.format(BASE_UUID, "2AA3"))
    val UUID_CHARACTERISTIC_BOND_MANAGEMENT_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2AA4"))
    val UUID_CHARACTERISTIC_BOND_MANAGEMENT_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2AA5"))
    val UUID_CHARACTERISTIC_CENTRAL_ADDRESS_RESOLUTION =
        UUID.fromString(String.format(BASE_UUID, "2AA6"))
    val UUID_CHARACTERISTIC_CGM_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2AA7"))
    val UUID_CHARACTERISTIC_CGM_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2AA8"))
    val UUID_CHARACTERISTIC_CGM_STATUS =
        UUID.fromString(String.format(BASE_UUID, "2AA9"))
    val UUID_CHARACTERISTIC_CGM_SESSION_START_TIME =
        UUID.fromString(String.format(BASE_UUID, "2AAA"))
    val UUID_CHARACTERISTIC_CGM_SESSION_RUN_TIME =
        UUID.fromString(String.format(BASE_UUID, "2AAB"))
    val UUID_CHARACTERISTIC_CGM_SPECIFIC_OPS_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2AAC"))
    val UUID_CHARACTERISTIC_INDOOR_POSITIONING_CONFIGURATION =
        UUID.fromString(String.format(BASE_UUID, "2AAD"))
    val UUID_CHARACTERISTIC_LATITUDE =
        UUID.fromString(String.format(BASE_UUID, "2AAE"))
    val UUID_CHARACTERISTIC_LONGITUDE =
        UUID.fromString(String.format(BASE_UUID, "2AAF"))
    val UUID_CHARACTERISTIC_LOCAL_NORTH_COORDINATE =
        UUID.fromString(String.format(BASE_UUID, "2AB0"))
    val UUID_CHARACTERISTIC_LOCAL_EAST_COORDINATE =
        UUID.fromString(String.format(BASE_UUID, "2AB1"))
    val UUID_CHARACTERISTIC_FLOOR_NUMBER =
        UUID.fromString(String.format(BASE_UUID, "2AB2"))
    val UUID_CHARACTERISTIC_ALTITUDE =
        UUID.fromString(String.format(BASE_UUID, "2AB3"))
    val UUID_CHARACTERISTIC_UNCERTAINTY =
        UUID.fromString(String.format(BASE_UUID, "2AB4"))
    val UUID_CHARACTERISTIC_LOCATION_NAME =
        UUID.fromString(String.format(BASE_UUID, "2AB5"))
    val UUID_CHARACTERISTIC_URI =
        UUID.fromString(String.format(BASE_UUID, "2AB6"))
    val UUID_CHARACTERISTIC_HTTP_HEADERS =
        UUID.fromString(String.format(BASE_UUID, "2AB7"))
    val UUID_CHARACTERISTIC_HTTP_STATUS_CODE =
        UUID.fromString(String.format(BASE_UUID, "2AB8"))
    val UUID_CHARACTERISTIC_HTTP_ENTITY_BODY =
        UUID.fromString(String.format(BASE_UUID, "2AB9"))
    val UUID_CHARACTERISTIC_HTTP_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2ABA"))
    val UUID_CHARACTERISTIC_HTTPS_SECURITY =
        UUID.fromString(String.format(BASE_UUID, "2ABB"))
    val UUID_CHARACTERISTIC_TDS_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2ABC"))
    val UUID_CHARACTERISTIC_OTS_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2ABD"))
    val UUID_CHARACTERISTIC_OBJECT_NAME =
        UUID.fromString(String.format(BASE_UUID, "2ABE"))
    val UUID_CHARACTERISTIC_OBJECT_TYPE =
        UUID.fromString(String.format(BASE_UUID, "2ABF"))
    val UUID_CHARACTERISTIC_OBJECT_SIZE =
        UUID.fromString(String.format(BASE_UUID, "2AC0"))
    val UUID_CHARACTERISTIC_OBJECT_FIRST_CREATED =
        UUID.fromString(String.format(BASE_UUID, "2AC1"))
    val UUID_CHARACTERISTIC_OBJECT_LAST_MODIFIED =
        UUID.fromString(String.format(BASE_UUID, "2AC2"))
    val UUID_CHARACTERISTIC_OBJECT_ID =
        UUID.fromString(String.format(BASE_UUID, "2AC3"))
    val UUID_CHARACTERISTIC_OBJECT_PROPERTIES =
        UUID.fromString(String.format(BASE_UUID, "2AC4"))
    val UUID_CHARACTERISTIC_OBJECT_ACTIONCONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2AC5"))
    val UUID_CHARACTERISTIC_OBJECT_LIST_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2AC6"))
    val UUID_CHARACTERISTIC_OBJECT_LIST_FILTER =
        UUID.fromString(String.format(BASE_UUID, "2AC7"))
    val UUID_CHARACTERISTIC_OBJECT_CHANGED =
        UUID.fromString(String.format(BASE_UUID, "2AC8"))
    val UUID_CHARACTERISTIC_RESOLVABLE_PRIVATE_ADDRESS_ONLY =
        UUID.fromString(String.format(BASE_UUID, "2AC9"))
    val UUID_CHARACTERISTIC_UNSPECIFIED =
        UUID.fromString(String.format(BASE_UUID, "2ACA"))
    val UUID_CHARACTERISTIC_DIRECTORY_LISTING =
        UUID.fromString(String.format(BASE_UUID, "2ACB"))
    val UUID_CHARACTERISTIC_FITNESS_MACHINE_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2ACC"))
    val UUID_CHARACTERISTIC_TREADMILL_DATA =
        UUID.fromString(String.format(BASE_UUID, "2ACD"))
    val UUID_CHARACTERISTIC_CROSS_TRAINER_DATA =
        UUID.fromString(String.format(BASE_UUID, "2ACE"))
    val UUID_CHARACTERISTIC_STEP_CLIMBER_DATA =
        UUID.fromString(String.format(BASE_UUID, "2ACF"))
    val UUID_CHARACTERISTIC_STAIR_CLIMBER_DATA =
        UUID.fromString(String.format(BASE_UUID, "2AD0"))
    val UUID_CHARACTERISTIC_ROWER_DATA =
        UUID.fromString(String.format(BASE_UUID, "2AD1"))
    val UUID_CHARACTERISTIC_INDOOR_BIKE_DATA =
        UUID.fromString(String.format(BASE_UUID, "2AD2"))
    val UUID_CHARACTERISTIC_TRAINING_STATUS =
        UUID.fromString(String.format(BASE_UUID, "2AD3"))
    val UUID_CHARACTERISTIC_SUPPORTED_SPEED_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2AD4"))
    val UUID_CHARACTERISTIC_SUPPORTED_INCLINATION_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2AD5"))
    val UUID_CHARACTERISTIC_SUPPORTED_RESISTANCE_LEVEL_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2AD6"))
    val UUID_CHARACTERISTIC_SUPPORTED_HEART_RATE_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2AD7"))
    val UUID_CHARACTERISTIC_SUPPORTED_POWER_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2AD8"))
    val UUID_CHARACTERISTIC_FITNESS_MACHINE_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2AD9"))
    val UUID_CHARACTERISTIC_FITNESS_MACHINE_STATUS =
        UUID.fromString(String.format(BASE_UUID, "2ADA"))
    val UUID_CHARACTERISTIC_MESH_PROVISIONING_DATA_IN =
        UUID.fromString(String.format(BASE_UUID, "2ADB"))
    val UUID_CHARACTERISTIC_MESH_PROVISIONING_DATA_OUT =
        UUID.fromString(String.format(BASE_UUID, "2ADC"))
    val UUID_CHARACTERISTIC_MESH_PROXY_DATA_IN =
        UUID.fromString(String.format(BASE_UUID, "2ADD"))
    val UUID_CHARACTERISTIC_MESH_PROXY_DATA_OUT =
        UUID.fromString(String.format(BASE_UUID, "2ADE"))
    val UUID_CHARACTERISTIC_AVERAGE_CURRENT =
        UUID.fromString(String.format(BASE_UUID, "2AE0"))
    val UUID_CHARACTERISTIC_AVERAGE_VOLTAGE =
        UUID.fromString(String.format(BASE_UUID, "2AE1"))
    val UUID_CHARACTERISTIC_BOOLEAN =
        UUID.fromString(String.format(BASE_UUID, "2AE2"))
    val UUID_CHARACTERISTIC_CHROMATIC_DISTANCE_FROM_PLANCKIAN =
        UUID.fromString(String.format(BASE_UUID, "2AE3"))
    val UUID_CHARACTERISTIC_CHROMATICITY_COORDINATES =
        UUID.fromString(String.format(BASE_UUID, "2AE4"))
    val UUID_CHARACTERISTIC_CHROMATICITY_IN_CCT_AND_DUV_VALUES =
        UUID.fromString(String.format(BASE_UUID, "2AE5"))
    val UUID_CHARACTERISTIC_CHROMATICITY_TOLERANCE =
        UUID.fromString(String.format(BASE_UUID, "2AE6"))
    val UUID_CHARACTERISTIC_CIE_13_3_1995_COLOR_RENDERING_INDEX =
        UUID.fromString(String.format(BASE_UUID, "2AE7"))
    val UUID_CHARACTERISTIC_COEFFICIENT =
        UUID.fromString(String.format(BASE_UUID, "2AE8"))
    val UUID_CHARACTERISTIC_CORRELATED_COLOR_TEMPERATURE =
        UUID.fromString(String.format(BASE_UUID, "2AE9"))
    val UUID_CHARACTERISTIC_COUNT_16 =
        UUID.fromString(String.format(BASE_UUID, "2AEA"))
    val UUID_CHARACTERISTIC_COUNT_24 =
        UUID.fromString(String.format(BASE_UUID, "2AEB"))
    val UUID_CHARACTERISTIC_COUNTRY_CODE =
        UUID.fromString(String.format(BASE_UUID, "2AEC"))
    val UUID_CHARACTERISTIC_DATE_UTC =
        UUID.fromString(String.format(BASE_UUID, "2AED"))
    val UUID_CHARACTERISTIC_ELECTRIC_CURRENT =
        UUID.fromString(String.format(BASE_UUID, "2AEE"))
    val UUID_CHARACTERISTIC_ELECTRIC_CURRENT_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2AEF"))
    val UUID_CHARACTERISTIC_ELECTRIC_CURRENT_SPECIFICATION =
        UUID.fromString(String.format(BASE_UUID, "2AF0"))
    val UUID_CHARACTERISTIC_ELECTRIC_CURRENT_STATISTICS =
        UUID.fromString(String.format(BASE_UUID, "2AF1"))
    val UUID_CHARACTERISTIC_ENERGY =
        UUID.fromString(String.format(BASE_UUID, "2AF2"))
    val UUID_CHARACTERISTIC_ENERGY_IN_A_PERIOD_OF_DAY =
        UUID.fromString(String.format(BASE_UUID, "2AF3"))
    val UUID_CHARACTERISTIC_EVENT_STATISTICS =
        UUID.fromString(String.format(BASE_UUID, "2AF4"))
    val UUID_CHARACTERISTIC_FIXED_STRING_16 =
        UUID.fromString(String.format(BASE_UUID, "2AF5"))
    val UUID_CHARACTERISTIC_FIXED_STRING_24 =
        UUID.fromString(String.format(BASE_UUID, "2AF6"))
    val UUID_CHARACTERISTIC_FIXED_STRING_36 =
        UUID.fromString(String.format(BASE_UUID, "2AF7"))
    val UUID_CHARACTERISTIC_FIXED_STRING_8 =
        UUID.fromString(String.format(BASE_UUID, "2AF8"))
    val UUID_CHARACTERISTIC_GENERIC_LEVEL =
        UUID.fromString(String.format(BASE_UUID, "2AF9"))
    val UUID_CHARACTERISTIC_GLOBAL_TRADE_ITEM_NUMBER =
        UUID.fromString(String.format(BASE_UUID, "2AFA"))
    val UUID_CHARACTERISTIC_ILLUMINANCE =
        UUID.fromString(String.format(BASE_UUID, "2AFB"))
    val UUID_CHARACTERISTIC_LUMINOUS_EFFICACY =
        UUID.fromString(String.format(BASE_UUID, "2AFC"))
    val UUID_CHARACTERISTIC_LUMINOUS_ENERGY =
        UUID.fromString(String.format(BASE_UUID, "2AFD"))
    val UUID_CHARACTERISTIC_LUMINOUS_EXPOSURE =
        UUID.fromString(String.format(BASE_UUID, "2AFE"))
    val UUID_CHARACTERISTIC_LUMINOUS_FLUX =
        UUID.fromString(String.format(BASE_UUID, "2AFF"))
    val UUID_CHARACTERISTIC_LUMINOUS_FLUX_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2B00"))
    val UUID_CHARACTERISTIC_LUMINOUS_INTENSITY =
        UUID.fromString(String.format(BASE_UUID, "2B01"))
    val UUID_CHARACTERISTIC_MASS_FLOW =
        UUID.fromString(String.format(BASE_UUID, "2B02"))
    val UUID_CHARACTERISTIC_PERCEIVED_LIGHTNESS =
        UUID.fromString(String.format(BASE_UUID, "2B03"))
    val UUID_CHARACTERISTIC_PERCENTAGE_8 =
        UUID.fromString(String.format(BASE_UUID, "2B04"))
    val UUID_CHARACTERISTIC_POWER =
        UUID.fromString(String.format(BASE_UUID, "2B05"))
    val UUID_CHARACTERISTIC_POWER_SPECIFICATION =
        UUID.fromString(String.format(BASE_UUID, "2B06"))
    val UUID_CHARACTERISTIC_RELATIVE_RUNTIME_IN_A_CURRENT_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2B07"))
    val UUID_CHARACTERISTIC_RELATIVE_RUNTIME_IN_A_GENERIC_LEVEL_RANGE = UUID.fromString(
        String.format(BASE_UUID, "2B08")
    )
    val UUID_CHARACTERISTIC_RELATIVE_VALUE_IN_A_VOLTAGE_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2B09"))
    val UUID_CHARACTERISTIC_RELATIVE_VALUE_IN_AN_ILLUMINANCE_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2B0A"))
    val UUID_CHARACTERISTIC_RELATIVE_VALUE_IN_A_PERIOD_OF_DAY =
        UUID.fromString(String.format(BASE_UUID, "2B0B"))
    val UUID_CHARACTERISTIC_RELATIVE_VALUE_IN_A_TEMPERATURE_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2B0C"))
    val UUID_CHARACTERISTIC_TEMPERATURE_8 =
        UUID.fromString(String.format(BASE_UUID, "2B0D"))
    val UUID_CHARACTERISTIC_TEMPERATURE_8_IN_A_PERIOD_OF_DAY =
        UUID.fromString(String.format(BASE_UUID, "2B0E"))
    val UUID_CHARACTERISTIC_TEMPERATURE_8_STATISTICS =
        UUID.fromString(String.format(BASE_UUID, "2B0F"))
    val UUID_CHARACTERISTIC_TEMPERATURE_RANGE =
        UUID.fromString(String.format(BASE_UUID, "2B10"))
    val UUID_CHARACTERISTIC_TEMPERATURE_STATISTICS =
        UUID.fromString(String.format(BASE_UUID, "2B11"))
    val UUID_CHARACTERISTIC_TIME_DECIHOUR_8 =
        UUID.fromString(String.format(BASE_UUID, "2B12"))
    val UUID_CHARACTERISTIC_TIME_EXPONENTIAL_8 =
        UUID.fromString(String.format(BASE_UUID, "2B13"))
    val UUID_CHARACTERISTIC_TIME_HOUR_24 =
        UUID.fromString(String.format(BASE_UUID, "2B14"))
    val UUID_CHARACTERISTIC_TIME_MILLISECOND_24 =
        UUID.fromString(String.format(BASE_UUID, "2B15"))
    val UUID_CHARACTERISTIC_TIME_SECOND_16 =
        UUID.fromString(String.format(BASE_UUID, "2B16"))
    val UUID_CHARACTERISTIC_TIME_SECOND_8 =
        UUID.fromString(String.format(BASE_UUID, "2B17"))
    val UUID_CHARACTERISTIC_VOLTAGE =
        UUID.fromString(String.format(BASE_UUID, "2B18"))
    val UUID_CHARACTERISTIC_VOLTAGE_SPECIFICATION =
        UUID.fromString(String.format(BASE_UUID, "2B19"))
    val UUID_CHARACTERISTIC_VOLTAGE_STATISTICS =
        UUID.fromString(String.format(BASE_UUID, "2B1A"))
    val UUID_CHARACTERISTIC_VOLUME_FLOW =
        UUID.fromString(String.format(BASE_UUID, "2B1B"))
    val UUID_CHARACTERISTIC_CHROMATICITY_COORDINATE =
        UUID.fromString(String.format(BASE_UUID, "2B1C"))
    val UUID_CHARACTERISTIC_RC_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2B1D"))
    val UUID_CHARACTERISTIC_RC_SETTINGS =
        UUID.fromString(String.format(BASE_UUID, "2B1E"))
    val UUID_CHARACTERISTIC_RECONNECTION_CONFIGURATION_CONTROL_POINT = UUID.fromString(
        String.format(BASE_UUID, "2B1F")
    )
    val UUID_CHARACTERISTIC_IDD_STATUS_CHANGED =
        UUID.fromString(String.format(BASE_UUID, "2B20"))
    val UUID_CHARACTERISTIC_IDD_STATUS =
        UUID.fromString(String.format(BASE_UUID, "2B21"))
    val UUID_CHARACTERISTIC_IDD_ANNUNCIATION_STATUS =
        UUID.fromString(String.format(BASE_UUID, "2B22"))
    val UUID_CHARACTERISTIC_IDD_FEATURES =
        UUID.fromString(String.format(BASE_UUID, "2B23"))
    val UUID_CHARACTERISTIC_IDD_STATUS_READER_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2B24"))
    val UUID_CHARACTERISTIC_IDD_COMMAND_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2B25"))
    val UUID_CHARACTERISTIC_IDD_COMMAND_DATA =
        UUID.fromString(String.format(BASE_UUID, "2B26"))
    val UUID_CHARACTERISTIC_IDD_RECORD_ACCESS_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2B27"))
    val UUID_CHARACTERISTIC_IDD_HISTORY_DATA =
        UUID.fromString(String.format(BASE_UUID, "2B28"))
    val UUID_CHARACTERISTIC_CLIENT_SUPPORTED_FEATURES =
        UUID.fromString(String.format(BASE_UUID, "2B29"))
    val UUID_CHARACTERISTIC_DATABASE_HASH =
        UUID.fromString(String.format(BASE_UUID, "2B2A"))
    val UUID_CHARACTERISTIC_BSS_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2B2B"))
    val UUID_CHARACTERISTIC_BSS_RESPONSE =
        UUID.fromString(String.format(BASE_UUID, "2B2C"))
    val UUID_CHARACTERISTIC_EMERGENCY_ID =
        UUID.fromString(String.format(BASE_UUID, "2B2D"))
    val UUID_CHARACTERISTIC_EMERGENCY_TEXT =
        UUID.fromString(String.format(BASE_UUID, "2B2E"))
    val UUID_CHARACTERISTIC_ENHANCED_BLOOD_PRESSURE_MEASUREMENT =
        UUID.fromString(String.format(BASE_UUID, "2B34"))
    val UUID_CHARACTERISTIC_ENHANCED_INTERMEDIATE_CUFF_PRESSURE =
        UUID.fromString(String.format(BASE_UUID, "2B35"))
    val UUID_CHARACTERISTIC_BLOOD_PRESSURE_RECORD =
        UUID.fromString(String.format(BASE_UUID, "2B36"))
    val UUID_CHARACTERISTIC_BR_EDR_HANDOVER_DATA =
        UUID.fromString(String.format(BASE_UUID, "2B38"))
    val UUID_CHARACTERISTIC_BLUETOOTH_SIG_DATA =
        UUID.fromString(String.format(BASE_UUID, "2B39"))
    val UUID_CHARACTERISTIC_SERVER_SUPPORTED_FEATURES =
        UUID.fromString(String.format(BASE_UUID, "2B3A"))
    val UUID_CHARACTERISTIC_PHYSICAL_ACTIVITY_MONITOR_FEATURES =
        UUID.fromString(String.format(BASE_UUID, "2B3B"))
    val UUID_CHARACTERISTIC_GENERAL_ACTIVITY_INSTANTANEOUS_DATA =
        UUID.fromString(String.format(BASE_UUID, "2B3C"))
    val UUID_CHARACTERISTIC_GENERAL_ACTIVITY_SUMMARY_DATA =
        UUID.fromString(String.format(BASE_UUID, "2B3D"))
    val UUID_CHARACTERISTIC_CARDIORESPIRATORY_ACTIVITY_INSTANTANEOUS_DATA = UUID.fromString(
        String.format(BASE_UUID, "2B3E")
    )
    val UUID_CHARACTERISTIC_CARDIORESPIRATORY_ACTIVITY_SUMMARY_DATA = UUID.fromString(
        String.format(BASE_UUID, "2B3F")
    )
    val UUID_CHARACTERISTIC_STEP_COUNTER_ACTIVITY_SUMMARY_DATA =
        UUID.fromString(String.format(BASE_UUID, "2B40"))
    val UUID_CHARACTERISTIC_SLEEP_ACTIVITY_INSTANTANEOUS_DATA =
        UUID.fromString(String.format(BASE_UUID, "2B41"))
    val UUID_CHARACTERISTIC_SLEEP_ACTIVITY_SUMMARY_DATA =
        UUID.fromString(String.format(BASE_UUID, "2B42"))
    val UUID_CHARACTERISTIC_PHYSICAL_ACTIVITY_MONITOR_CONTROL_POINT = UUID.fromString(
        String.format(BASE_UUID, "2B43")
    )
    val UUID_CHARACTERISTIC_CURRENT_SESSION =
        UUID.fromString(String.format(BASE_UUID, "2B44"))
    val UUID_CHARACTERISTIC_SESSION =
        UUID.fromString(String.format(BASE_UUID, "2B45"))
    val UUID_CHARACTERISTIC_PREFERRED_UNITS =
        UUID.fromString(String.format(BASE_UUID, "2B46"))
    val UUID_CHARACTERISTIC_HIGH_RESOLUTION_HEIGHT =
        UUID.fromString(String.format(BASE_UUID, "2B47"))
    val UUID_CHARACTERISTIC_MIDDLE_NAME =
        UUID.fromString(String.format(BASE_UUID, "2B48"))
    val UUID_CHARACTERISTIC_STRIDE_LENGTH =
        UUID.fromString(String.format(BASE_UUID, "2B49"))
    val UUID_CHARACTERISTIC_HANDEDNESS =
        UUID.fromString(String.format(BASE_UUID, "2B4A"))
    val UUID_CHARACTERISTIC_DEVICE_WEARING_POSITION =
        UUID.fromString(String.format(BASE_UUID, "2B4B"))
    val UUID_CHARACTERISTIC_FOUR_ZONE_HEART_RATE_LIMITS =
        UUID.fromString(String.format(BASE_UUID, "2B4C"))
    val UUID_CHARACTERISTIC_HIGH_INTENSITY_EXERCISE_THRESHOLD =
        UUID.fromString(String.format(BASE_UUID, "2B4D"))
    val UUID_CHARACTERISTIC_ACTIVITY_GOAL =
        UUID.fromString(String.format(BASE_UUID, "2B4E"))
    val UUID_CHARACTERISTIC_SEDENTARY_INTERVAL_NOTIFICATION =
        UUID.fromString(String.format(BASE_UUID, "2B4F"))
    val UUID_CHARACTERISTIC_CALORIC_INTAKE =
        UUID.fromString(String.format(BASE_UUID, "2B50"))
    val UUID_CHARACTERISTIC_AUDIO_INPUT_STATE =
        UUID.fromString(String.format(BASE_UUID, "2B77"))
    val UUID_CHARACTERISTIC_GAIN_SETTINGS_ATTRIBUTE =
        UUID.fromString(String.format(BASE_UUID, "2B78"))
    val UUID_CHARACTERISTIC_AUDIO_INPUT_TYPE =
        UUID.fromString(String.format(BASE_UUID, "2B79"))
    val UUID_CHARACTERISTIC_AUDIO_INPUT_STATUS =
        UUID.fromString(String.format(BASE_UUID, "2B7A"))
    val UUID_CHARACTERISTIC_AUDIO_INPUT_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2B7B"))
    val UUID_CHARACTERISTIC_AUDIO_INPUT_DESCRIPTION =
        UUID.fromString(String.format(BASE_UUID, "2B7C"))
    val UUID_CHARACTERISTIC_VOLUME_STATE =
        UUID.fromString(String.format(BASE_UUID, "2B7D"))
    val UUID_CHARACTERISTIC_VOLUME_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2B7E"))
    val UUID_CHARACTERISTIC_VOLUME_FLAGS =
        UUID.fromString(String.format(BASE_UUID, "2B7F"))
    val UUID_CHARACTERISTIC_OFFSET_STATE =
        UUID.fromString(String.format(BASE_UUID, "2B80"))
    val UUID_CHARACTERISTIC_AUDIO_LOCATION =
        UUID.fromString(String.format(BASE_UUID, "2B81"))
    val UUID_CHARACTERISTIC_VOLUME_OFFSET_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2B82"))
    val UUID_CHARACTERISTIC_AUDIO_OUTPUT_DESCRIPTION =
        UUID.fromString(String.format(BASE_UUID, "2B83"))
    val UUID_CHARACTERISTIC_DEVICE_TIME_FEATURE =
        UUID.fromString(String.format(BASE_UUID, "2B8E"))
    val UUID_CHARACTERISTIC_DEVICE_TIME_PARAMETERS =
        UUID.fromString(String.format(BASE_UUID, "2B8F"))
    val UUID_CHARACTERISTIC_DEVICE_TIME =
        UUID.fromString(String.format(BASE_UUID, "2B90"))
    val UUID_CHARACTERISTIC_DEVICE_TIME_CONTROL_POINT =
        UUID.fromString(String.format(BASE_UUID, "2B91"))
    val UUID_CHARACTERISTIC_TIME_CHANGE_LOG_DATA =
        UUID.fromString(String.format(BASE_UUID, "2B92"))
}

