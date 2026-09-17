package com.example.bleex

import android.Manifest
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.bleex.bluetooth.AndroidBleScanner
import com.example.bleex.bluetooth.hasBlePermissions
import com.example.bleex.ui.ScanScreen
import com.example.bleex.viewmodel.ScanViewModel
import com.example.bleex.ui.StartScreen
import android.os.Build


enum class AppScreen{
    START,
    SCAN
}
@Composable
fun App() {

    var currentScreen by remember { mutableStateOf(AppScreen.START) } //switch to using Navigation Compose?

    val context = LocalContext.current

    //create a scanner
    val scanner = remember {
        AndroidBleScanner(context)
    }
    //create scanViewModel and pass it the scanner
    val scanViewModel: ScanViewModel = viewModel(
        factory = ScanViewModel.factory(scanner)
    )
    //collect devices from scanViewModel
    val devices by scanViewModel.devices.collectAsState()
    //track scanning state
    val isScanning by scanViewModel.isScanning.collectAsState()
    val error by scanViewModel.error.collectAsState()

    //permissions required
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        //android 12+
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else{
        //android 11 and lower
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


        // permission request
        val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        )  { permissions ->
            if (context.hasBlePermissions()) {
                scanViewModel.startScanning()
                currentScreen = AppScreen.SCAN
            }
        }

    //screen selection
    when (currentScreen){
        AppScreen.START-> StartScreen(
            onStartClick = {
                if (context.hasBlePermissions()) {
                    scanViewModel.startScanning()
                    currentScreen = AppScreen.SCAN
                } else {
                    permissionLauncher.launch(permissions)
                }
            }
        )

     AppScreen.SCAN -> ScanScreen(
        devices = devices,
        isScanning = isScanning,
        error = error,
        onStartScan = { scanViewModel.startScanning() },
        onStopScan = { scanViewModel.stopScanning() }
    )
}
}



