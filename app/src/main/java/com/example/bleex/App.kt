package com.example.bleex

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.bleex.bluetooth.BleDevice
import com.example.bleex.bluetooth.BleScanner
import com.example.bleex.bluetooth.hasBlePermissions
import com.example.bleex.ui.ScanScreen
import com.example.bleex.ui.StartScreen


@Composable
fun App() {
    var showScanScreen by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val devices = remember {
        mutableStateListOf<BleDevice>()
    }

    val scanner = remember {
        BleScanner(context) { device ->
            val index = devices.indexOfFirst {
                it.address == device.address
            }
            if (index == -1) {
                //new device
                devices.add(device)
            } else {
                //update existing device
                devices[index] = device
            }
        }
    }


        val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_SCAN) { permissions ->
            if (context.hasBlePermissions()) {
                scanner.startScan()
                showScanScreen = true
            }
        }

    if (showScanScreen) {
        ScanScreen(
            devices = devices
        )
    } else {
        StartScreen(
            onStartClick = @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_SCAN) {
                if (context.hasBlePermissions()) {
                    scanner.startScan()
                    showScanScreen = true
                } else {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            }
        )
    }
}


