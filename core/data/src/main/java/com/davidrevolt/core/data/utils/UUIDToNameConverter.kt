package com.davidrevolt.core.data.utils

import java.util.UUID


val GENERIC_ACCESS_SERVICE = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb")

class NamesResolver {
    companion object {
        val _services = HashMap<String, String>()
        val _characteristics = HashMap<String, String>()
    }

    init{
        _services["00001800-0000-1000-8000-00805f9b34fb"] = "Generic Access"
        _services.put("00001801-0000-1000-8000-00805f9b34fb", "Generic Attribute")

        _characteristics.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name")
        _characteristics.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");
        _characteristics.put("00002a04-0000-1000-8000-00805f9b34fb", "Peripheral Preferred Connection Parameters");


    }
}

