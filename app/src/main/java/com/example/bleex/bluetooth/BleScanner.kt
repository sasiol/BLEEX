package com.example.bleex.bluetooth

import kotlinx.coroutines.flow.Flow

interface BleScanner {
     fun scan(): Flow<BleDevice>
}