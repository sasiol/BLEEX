package com.example.bleex.ui

import androidx.annotation.RequiresPermission
import android.Manifest
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import kotlinx.coroutines.launch
import com.example.bleex.bluetooth.BleDevice
import com.example.bleex.bluetooth.BleScanner
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class ScanViewModel (
    private val scanner: BleScanner
): ViewModel(){

    //has private and public  list of devices using flow
    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())

    val devices: StateFlow<List<BleDevice>> = _devices.asStateFlow()

    //for collecting flow from scanner
    private var collectJob: Job? =null

    fun startScanning() {

        //if already running, dont start another scan
        if (collectJob != null) return

        collectJob = viewModelScope.launch{
            scanner.scan().collect { device ->
                onDeviceFound(device)
            }
        }
    }

    fun stopScanning() {
        collectJob?.cancel()
        collectJob = null
    }

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
    companion object {
        fun factory(scanner: BleScanner): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ScanViewModel(scanner)
                }
            }
    }
}


