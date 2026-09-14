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

        viewModel.onDeviceFound(testDevice1)

        assertEquals(listOf(testDevice1), viewModel.devices.value)
    }


    @Test
    fun `onDeviceFound updates existing device`() {
        val viewModel = ScanViewModel(TestBleScanner())
        val ogDevice = testDevice1
        val updatedDevice = testDevice1.copy(rssi = -70)
        viewModel.onDeviceFound(ogDevice)
        viewModel.onDeviceFound(updatedDevice)

        assertEquals(listOf(updatedDevice), viewModel.devices.value)
    }


    @Test
     fun `startScanning adds device from scanner`() =runTest {
        val scanner = TestBleScanner()
        val viewModel = ScanViewModel(
            scanner = scanner,
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        viewModel.startScanning()
        advanceUntilIdle()
        scanner.devices.emit(testDevice1)

        assertEquals(listOf(testDevice1), viewModel.devices.value)
    }

    @Test
    fun `stopScanning stops receiving devices from scanner`() =runTest {
        val scanner = TestBleScanner()
        val viewModel = ScanViewModel(
            scanner = scanner,
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        viewModel.startScanning()
        advanceUntilIdle()
        scanner.devices.emit(testDevice1)

        assertEquals(listOf(testDevice1), viewModel.devices.value)

        //stop scanning
        viewModel.stopScanning()
        advanceUntilIdle()
        scanner.devices.emit(testDevice2)

        assertEquals(listOf(testDevice1), viewModel.devices.value)

    }



}

private class TestBleScanner : BleScanner {
    val devices = MutableSharedFlow<BleDevice>()
    var bluetoothEnabled = true
    override fun scan(): Flow<BleDevice> = devices
    override fun isBluetoothEnabled(): Boolean {
        return bluetoothEnabled
    }
}

    private val testDevice1 = BleDevice(
        name = "Test Device",
        address = "AA:BB:CC:DD:EE:FF",
        rssi = -50,
        services = emptyList(),
        manufacturerData = emptyMap(),
        isConnectable = true

    )

    private val testDevice2 = BleDevice(
        name = "Test Device2",
        address = "GG:HH:II:JJ:KK:LL",
        rssi = -60,
        services = emptyList(),
        manufacturerData = emptyMap(),
        isConnectable = true
    )


