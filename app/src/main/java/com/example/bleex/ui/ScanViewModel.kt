package com.example.bleex.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.bleex.bluetooth.BleDevice


class ScanViewModel : ViewModel(){

    //has list of devices
    private val _devices = mutableStateListOf<BleDevice>()
    val devices: List<BleDevice>
        get() = _devices

    //update devices
    //when device is found, check if it is new device or not
    fun onDeviceFound(device: BleDevice) {
        val index = _devices.indexOfFirst {
            it.address == device.address
        }
        if (index == -1) {
            //add new device
            _devices.add(device)
        } else {
            //update old device
            _devices[index] = device
        }
    }
}