package com.example.bleex.ui
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bleex.ui.components.BleexButton
import com.example.bleex.ui.components.BleexTopBar


@Preview(showBackground = true)
@Composable
fun StartScreen(
    onStartClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BleexTopBar()

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            BleexButton(
                text = "START SCAN",
                onClick = onStartClick
            )
            }
        }
    }

