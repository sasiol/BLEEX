package com.example.bleex

import android.Manifest
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.bleex.bluetooth.BleDevice
import com.example.bleex.bluetooth.BleScanner
import com.example.bleex.bluetooth.hasBlePermissions
import com.example.bleex.ui.ScanScreen
import com.example.bleex.ui.ScanViewModel
import com.example.bleex.ui.StartScreen


@Composable
fun App() {

    var showScanScreen by remember { mutableStateOf(false) } //better way to do?

    val context = LocalContext.current

    //create a scanner for scanning bluetooth devices
    val scanner = remember {
        BleScanner(context)
    }
    //create scanViewModel and pass it the scanner
    val scanViewModel: ScanViewModel = viewModel(
        factory = ScanViewModel.factory(scanner)
    )
    //collect devices from scanViewModel
    val devices by scanViewModel.devices.collectAsState()
    //track scanning state
    val isScanning by scanViewModel.isScanning.collectAsState()


        // permission checks
        val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        )  { permissions ->
            if (context.hasBlePermissions()) {
                scanViewModel.startScanning()
                showScanScreen = true
            }
        }

    //switch to scanning screen
    if (showScanScreen) {
        ScanScreen(
            devices = devices,
            isScanning = isScanning,
            onStopScan = {
                scanViewModel.stopScanning()
                showScanScreen = false}
        )

        //starting screen
    } else {
        StartScreen(
            onStartClick = {
                if (context.hasBlePermissions()) {
                    scanViewModel.startScanning()
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



