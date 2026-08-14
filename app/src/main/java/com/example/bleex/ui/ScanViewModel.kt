package com.example.bleex.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.bleex.bluetooth.BleDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class ScanViewModel : ViewModel(){

    //has list of devices
    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())

    val devices: StateFlow<List<BleDevice>> = _devices.asStateFlow()

    //update devices
    //when device is found, check if it is new device or not
    fun onDeviceFound(device: BleDevice) {
        val index = _devices.value.indexOfFirst {
            it.address == device.address
        }
        if (index == -1) {
            //add new device by creating new list
            _devices.value= _devices.value + device
        } else {
            //update existing device by creating new list with updated values
            _devices.value= _devices.value.mapIndexed { i, existingDevice ->
                if (i==index) {
                device} else {
                    existingDevice
                }
            }
        }
    }
}