package com.example.bleex.bluetooth

data class BleDevice(
    val name: String,
    val address: String,
    val rssi: Int,
    val services: List<String>,
    val manufacturerData: Map<Int, ByteArray>,
    val isConnectable: Boolean

    //val estimatedDistance: Double?,
)
