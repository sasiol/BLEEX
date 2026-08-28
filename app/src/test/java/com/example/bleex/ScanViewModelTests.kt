package com.example.bleex

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bleex.bluetooth.BleDevice
import com.example.bleex.bluetooth.BleScanner
import com.example.bleex.ui.ScanViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Test
import org.junit.experimental.theories.suppliers.TestedOn

class ScanViewModelTests {

    @Test
    fun `onDeviceFound adds new device`(){
        val scanner=TestBleScanner()
        val viewModel= ScanViewModel(scanner)

        val device = BleDevice(
            name = "Test Device",
            address = "AA:BB:CC:DD:EE:FF",
            rssi = -50,
            services = emptyList()
        )
        viewModel.onDeviceFound(device)

        assertEquals(listOf(device),viewModel.devices.value)
    }
}

private class TestBleScanner : BleScanner {
    override fun scan(): Flow<BleDevice> {
        return emptyFlow()
    }
}