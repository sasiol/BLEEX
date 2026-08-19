package com.example.bleex.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.material3.Button


@Composable
fun ScanScreen(devices: List<BleDevice>, isScanning: Boolean, onStopScan:() -> Unit ) {

    var expandedDevice by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "BLEEX",
                fontSize = 32.sp
            )

            if (isScanning) {

                Text(
                    text = "Scanning"
                )
            }
        }

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

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expandedDevice =
                                if (isExpanded) {
                                    null
                                } else {
                                    device.address
                                }
                        }
                        .padding(
                            horizontal = 24.dp,
                            vertical = 6.dp
                        )
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = device.name,
                            fontSize = 25.sp
                        )

                        Text(
                            text = "${device.rssi} dBm" ,
                            fontSize = 25.sp
                        )
                    }

                    if (isExpanded) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Address: ${device.address}",
                            fontSize = 22.sp
                        )

                        Text(
                            text = "Signal strength: ${device.rssi} dBm",
                            fontSize = 25.sp
                        )

                        Text(
                            text = "Services: ${
                                device.services.joinToString()
                            }",
                            fontSize = 25.sp
                        )
                    }
                }
            }
        }
            }
        if (isScanning){
            ScanBorder()
        }
        Button(
            onClick = onStopScan,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Text("Stop scanning")
        }
    }
}


@Composable
fun ScanBorder() {

    val infiniteTransition = rememberInfiniteTransition(
        label = "scanning border"
    )

    val progress by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 4000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "border progress"
    )

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    top = 8.dp.toPx(),
                    left = 8.dp.toPx(),
                    right = size.width - 8.dp.toPx(),
                    bottom = size.height - 8.dp.toPx(),
                    cornerRadius = CornerRadius(
                        x = 20.dp.toPx(),
                        y = 20.dp.toPx()
                    )
                )
            )
        }

        val pathMeasure = PathMeasure()
        pathMeasure.setPath(path, false)
        val pathLength = pathMeasure.length
        val start = progress * pathLength
        val segment = Path()

        pathMeasure.getSegment(
            startDistance = start,
            stopDistance = start + 500.dp.toPx(),
            destination = segment,
            startWithMoveTo = true
        )
        drawPath(
            path = segment,
            color = Color.Blue,
            style = Stroke(width = 5.dp.toPx())
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

    ScanScreen(devices = fakeDevices, isScanning = true, onStopScan= {})
}

