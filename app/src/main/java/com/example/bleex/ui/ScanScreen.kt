package com.example.bleex.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bleex.bluetooth.BleDevice
import com.example.bleex.ui.components.BleDeviceCard
import com.example.bleex.ui.components.BleexButton
import com.example.bleex.ui.components.BleexTopBar
import com.example.bleex.ui.components.ScanBorder


@Composable
fun ScanScreen(devices: List<BleDevice>,
               isScanning: Boolean,
               error:String?,
               onStartScan: () -> Unit,
               onStopScan:() -> Unit ) {

    var expandedDevice by remember { mutableStateOf<String?>(null) }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (error != null) {
                Text(text = error,
                    modifier = Modifier.padding(16.dp)
                )
            }

        // Top bar
            BleexTopBar()

        // Device list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 20.dp,
                bottom = 40.dp
            )
        ) {
            items(devices) { device ->
                val isExpanded = expandedDevice == device.address

                BleDeviceCard(
                    device = device,
                    expanded = isExpanded,
                    onClick = {
                        expandedDevice =
                            if (isExpanded) {
                                null
                            } else {
                                device.address
                            }
                    }
                )
            }
        }
            }
        if (isScanning){
            ScanBorder()
        }
        BleexButton(
            text = if (isScanning) "STOP SCAN" else "START SCAN",
            onClick = {
                if (isScanning) {
                    onStopScan()
                } else {
                    onStartScan()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}




@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    val fakeDevices = listOf(
        BleDevice(
            name = "Pixel Buds",
            address = "AA:BB:CC:01",
            rssi = -42,
            services = listOf("Battery Service"),
            manufacturerData = emptyMap(),
            isConnectable = true

        ),
        BleDevice(
            name = "Samsung TV",
            address = "AA:BB:CC:02",
            rssi = -67,
            //estimatedDistance = null,
            services = listOf("Device Information"),
            manufacturerData = emptyMap(),
            isConnectable = true
        ),
        BleDevice(
            name = "Garmin Watch",
            address = "AA:BB:CC:03",
            rssi = -55,
            services = listOf("Battery Service", "Heart Rate"),
            manufacturerData = mapOf(
                0x004C to byteArrayOf(
                    0x02,
                    0x15,
                    0x7A,
                    0x3B
                )
            ),isConnectable = true,

        )
    )
    var isScanning by remember { mutableStateOf(true) }
    ScanScreen(devices = fakeDevices, isScanning = isScanning, error=null, onStartScan = {isScanning = true}, onStopScan= {isScanning = false})
}

