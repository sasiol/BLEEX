package com.example.bleex.bluetooth

import android.Manifest
import android.bluetooth.le.ScanCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission

/**
 * Handles BLE device scanning.
 * Receives scan results from Android's Bluetooth APIs
 * Creates BleDevice objects and passes them to the app when devices are found.
 */
class BleScanner(
    private val context: Context,
    private val onDeviceFound: (BleDevice) -> Unit

        ) {
    private val bluetoothManager =context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter
    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    private var scanning = false

    private val scanCallback = object : ScanCallback() {

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onScanResult(
            callbackType: Int,
            result: ScanResult
        ) {
            val name = result.device.name ?: "Unknown device" //incase the device name is null

            val device = BleDevice(
                name = name,
                address = result.device.address,
                rssi = result.rssi,
                //estimatedDistance = null,
                services = emptyList()
            )

            onDeviceFound(device)
            Log.d(
                "BLE",
                "Name: $name, Address: ${result.device.address}, RSSI: ${result.rssi}"
            )
        }
    }

    // Functions
    fun startScan() {
        if (scanning) return
        Log.d("BLE", "startScan() called")

        if (!context.hasBlePermissions()) {
            Log.d("BLE", "Missing permissions")
            return
        }

        if (scanning) {
            Log.d("BLE", "Already scanning")
            return
        }

        try {
            bluetoothLeScanner.startScan(scanCallback)
            scanning = true
        } catch (e: SecurityException) {
            Log.e("BLE", "Missing Bluetooth permission", e)
        }
    }

    fun stopScan() {
        if (!scanning) return
        try {
            bluetoothLeScanner.stopScan(scanCallback)
            scanning = false
        }catch   (e: SecurityException) {
            Log.e("BLE", "Missing Bluetooth permission", e)
            }
    }
}


