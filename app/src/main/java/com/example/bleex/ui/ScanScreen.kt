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

@Preview(showBackground = true)
@Composable
fun ScanScreen() {
    //fakes for now
    val devices = listOf(
        BleDevice("Pixel Buds", "-42", "AA:BB:CC:01"),
        BleDevice("Samsung TV", "-67", "AA:BB:CC:02"),
        BleDevice("Garmin Watch", "-55", "AA:BB:CC:03")
    )

    var expandedDevice by remember {mutableStateOf<String?>(null)}
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
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
data class BleDevice(
    val name: String,
    val address: String,
    val rssi: String,
    //val estimatedDistance: Double?,
)
