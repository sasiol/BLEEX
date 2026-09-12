package com.example.bleex

import com.example.bleex.bluetooth.BleDevice
import com.example.bleex.bluetooth.BleScanner
import com.example.bleex.viewmodel.ScanViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Test
import kotlinx.coroutines.test.runTest

class ScanViewModelTests {

    @Test
    fun `onDeviceFound adds new device`() {
        val viewModel = ScanViewModel(TestBleScanner())

        val device = BleDevice(
            name = "Test Device",
            address = "AA:BB:CC:DD:EE:FF",
            rssi = -50,
            services = emptyList()
        )
        viewModel.onDeviceFound(device)

        assertEquals(listOf(device), viewModel.devices.value)
    }


    @Test
    fun `onDeviceFound updates existing device`() {
        val viewModel = ScanViewModel(TestBleScanner())

        val ogDevice = BleDevice(
            name = "Test Device",
            address = "AA:BB:CC:DD:EE:FF",
            rssi = -50,
            services = emptyList()
        )

        val upDevice = BleDevice(
            name = "Test Device",
            address = "AA:BB:CC:DD:EE:FF",
            rssi = -70,
            services = emptyList()
        )
        viewModel.onDeviceFound(ogDevice)
        viewModel.onDeviceFound(upDevice)

        assertEquals(listOf(upDevice), viewModel.devices.value)
    }


    @Test
     fun `startScanning adds device from scanner`() =runTest {
        val scanner = TestBleScanner()
        val viewModel = ScanViewModel(
            scanner = scanner,
            dispatcher = StandardTestDispatcher(testScheduler)
        )
        val device = BleDevice(
            name = "Test Device",
            address = "AA:BB:CC:DD:EE:FF",
            rssi = -50,
            services = emptyList()
        )

        viewModel.startScanning()
        advanceUntilIdle()
        scanner.devices.emit(device)

        assertEquals(listOf(device), viewModel.devices.value)
    }

    @Test
    fun `stopScanning stops receiving devices from scanner`() =runTest {
        val scanner = TestBleScanner()
        val viewModel = ScanViewModel(
            scanner = scanner,
            dispatcher = StandardTestDispatcher(testScheduler)
        )
        val device = BleDevice(
            name = "Test Device",
            address = "AA:BB:CC:DD:EE:FF",
            rssi = -50,
            services = emptyList()
        )
        val stoppedDevice = BleDevice(
            name = "stopped Device",
            address = "AA:BB:CC:DD:EE:FF",
            rssi = -60,
            services = emptyList()
        )

        viewModel.startScanning()
        advanceUntilIdle()
        scanner.devices.emit(device)

        assertEquals(listOf(device), viewModel.devices.value)

        //stop scanning
        viewModel.stopScanning()
        advanceUntilIdle()
        scanner.devices.emit(stoppedDevice)

        assertEquals(listOf(device), viewModel.devices.value)

    }



}

private class TestBleScanner : BleScanner {
    val devices = MutableSharedFlow<BleDevice>()

    override fun scan(): Flow<BleDevice> = devices
}