package com.example.bleex.bluetooth

import android.Manifest
import android.bluetooth.le.ScanCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * Handles BLE device scanning.
 * Receives scan results from Android's Bluetooth APIs
 * Creates BleDevice objects and passes them to the app when devices are found.
 */
class AndroidBleScanner(
    private val context: Context
        ): BleScanner {

    private val bluetoothManager =context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter
    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    //create a flow that devices are put into when found
    override fun scan(): Flow<BleDevice> =callbackFlow {
        //check permission
        if (!context.hasBlePermissions()) {
            close()
            return@callbackFlow
        }

         val scanCallback = object : ScanCallback() {


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
                //send device to flow
                trySend(device)
                Log.d(
                    "BLE",
                    "Name: $name, Address: ${result.device.address}, RSSI: ${result.rssi}"
                )
            }
        }
        try {
            bluetoothLeScanner.startScan(scanCallback)
        } catch (e: SecurityException) {
            close(e)
            return@callbackFlow
        }

        awaitClose {
            try {
                bluetoothLeScanner.stopScan(scanCallback)
            } catch (e: SecurityException) {
                Log.e("BLE", "Could not stop BLE scan", e)
            }
        }
    }
}

