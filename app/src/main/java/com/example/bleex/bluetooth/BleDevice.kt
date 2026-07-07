package com.example.bleex.bluetooth

data class BleDevice(
    val name: String,
    val address: String,
    val rssi: Int,
    //val estimatedDistance: Double?,
    val services: List<String>
)
