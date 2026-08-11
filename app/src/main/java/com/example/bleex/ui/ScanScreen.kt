package com.example.bleex.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.bleex.bluetooth.BleScanner

@Composable
fun ScanScreen(devices: List<BleDevice>) {

    var expandedDevice by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (devices.isEmpty()) {
            Text("No devices found")
        } else {
            LazyColumn {
                items(devices) { device ->
                    val isExpanded = expandedDevice == device.address

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedDevice =
                                    if (isExpanded) null else device.address
                            }
                            .padding(16.dp)
                    ) {

                        Text(text = device.name)

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Address: ${device.address}")
                            Text("Signal strength: ${device.rssi}")
                            Text("Service: Battery, Device Info")
                        }
                    }
                }
            }
        }
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
            //estimatedDistance = null,
            services = listOf("Battery Service")
        ),
        BleDevice(
            name = "Samsung TV",
            address = "AA:BB:CC:02",
            rssi = -67,
            //estimatedDistance = null,
            services = listOf("Device Information")
        ),
        BleDevice(
            name = "Garmin Watch",
            address = "AA:BB:CC:03",
            rssi = -55,
            //estimatedDistance = null,
            services = listOf("Battery Service", "Heart Rate")
        )
    )

    ScanScreen(devices = fakeDevices)
}

