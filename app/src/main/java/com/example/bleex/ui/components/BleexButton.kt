package com.example.bleex.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BleexButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(160.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0D2A4A)
        )
    ) {
        Text(
            text = text,
            color = Color.White
        )
    }
}