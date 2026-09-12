package com.example.bleex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bleex.bluetooth.BleDevice

@Composable
fun BleDeviceCard(
    device: BleDevice,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .padding(
                horizontal = 24.dp,
                vertical = 6.dp
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                    text = "${device.rssi} dBm",
                    fontSize = 25.sp
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Address: ${device.address}",
                    fontSize = 22.sp
                )

                Text(
                    text = "Signal strength: ${device.rssi} dBm",
                    fontSize = 25.sp
                )

                Text(
                    text = "Services: ${device.services.joinToString()}",
                    fontSize = 25.sp
                )
            }
        }
    }
}