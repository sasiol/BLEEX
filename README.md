# BLEEX

BLEEX is a small Android application for discovering nearby Bluetooth Low Energy (BLE) devices.

The project was built as a learning project to explore BLE scanning, Kotlin coroutines and Flow, Jetpack Compose, and ViewModel-based architecture.

## Features

- Scan for nearby Bluetooth Low Energy devices
- Display discovered device name, address, and RSSI
- Update information for previously discovered devices
- Expand devices to view additional information
- Start and stop BLE scanning
- Handle Bluetooth runtime permissions
- Animated scanning indicator


## Tech Stack

- Kotlin
- Jetpack Compose
- Android ViewModel
- Kotlin Coroutines
- Kotlin Flow / `callbackFlow`
- Android Bluetooth Low Energy APIs

## Project Structure
```text
com.example.bleex
├── App.kt
├── MainActivity.kt
│
├── bluetooth
│   ├── BleDevice.kt
│   ├── BlePermissions.kt
│   └── BleScanner.kt
│
└── ui
    ├── ScanScreen.kt
    ├── ScanViewModel.kt
    ├── StartScreen.kt
    └── theme
```
## Future Improvements

- Improve scan error handling
- Add ViewModel unit tests
- Improve scanning state and lifecycle handling
- Add filtering and sorting of discovered devices
- Improve UI 

    

