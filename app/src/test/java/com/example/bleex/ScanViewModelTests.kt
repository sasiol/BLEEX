package com.example.bleex

import com.example.bleex.bluetooth.BleDevice
import com.example.bleex.bluetooth.BleScanner
import com.example.bleex.viewmodel.ScanViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Test
import kotlinx.coroutines.test.runTest

class ScanViewModelTests {

    @Test
    fun `onDeviceFound adds new device`() = runTest {
        val viewModel = ScanViewModel(TestBleScanner(), dispatcher = StandardTestDispatcher(testScheduler))

        viewModel.onDeviceFound(testDevice1)

        assertEquals(listOf(testDevice1), viewModel.devices.value)
    }


    @Test
    fun `onDeviceFound updates existing device`() = runTest{
        val viewModel = ScanViewModel(TestBleScanner(), dispatcher = StandardTestDispatcher(testScheduler))
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

    @Test
    fun `startScanning does not scan when Bluetooth is disabled`() =runTest {
        val scanner = TestBleScanner()
        scanner.bluetoothEnabled=false

        val viewModel = ScanViewModel(
            scanner = scanner,
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        viewModel.startScanning()
        advanceUntilIdle()
        scanner.devices.emit(testDevice1)

        assertEquals("Bluetooth is turned off", viewModel.error.value)
        assertEquals(false, viewModel.isScanning.value)
        assertTrue(viewModel.devices.value.isEmpty())
    }

    @Test
    fun `startScanning does not start another scan when already scanning`() = runTest {
        val scanner = TestBleScanner()

        val viewModel = ScanViewModel(
            scanner = scanner,
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        viewModel.startScanning()
        advanceUntilIdle()

        viewModel.startScanning()
        advanceUntilIdle()

        assertEquals(1, scanner.scanCount)
    }

    @Test
    fun `bluetooth turning off stops scanning`() = runTest {
        val scanner = TestBleScanner()

        val viewModel = ScanViewModel(
            scanner = scanner,
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        viewModel.startScanning()
        advanceUntilIdle()

        assertEquals(true, viewModel.isScanning.value)

        scanner.bluetoothStates.emit(false)
        advanceUntilIdle()

        assertEquals(false, viewModel.isScanning.value)
        assertEquals("Bluetooth is turned off", viewModel.error.value)
    }



}

private class TestBleScanner : BleScanner {
    val devices = MutableSharedFlow<BleDevice>()
    val bluetoothStates = MutableSharedFlow<Boolean>()

    var bluetoothEnabled = true
    var scanCount=0
    override fun scan(): Flow<BleDevice> {
        scanCount++
        return devices
    }
    override fun isBluetoothEnabled(): Boolean {
        return bluetoothEnabled
    }
    override fun bluetoothState(): Flow<Boolean> = bluetoothStates

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


