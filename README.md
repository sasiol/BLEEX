# BLEEX

BLEEX is a small Android application for discovering nearby Bluetooth Low Energy (BLE) devices.

The project was built as a learning project to explore Android development, BLE scanning, Kotlin coroutines and Flow, Jetpack Compose, ViewModel-based architecture, testing, and CI security tooling.

## Features

- Scan for nearby Bluetooth Low Energy devices
- Display discovered device name, address, and RSSI
- Update information for previously discovered devices
- Expand devices to view additional information
- Start and stop BLE scanning
- Handle Bluetooth runtime permissions
- Animated scanning indicator


## Screenshots

### Scanning Screen
<img width="1080" height="2106" alt="BLEEX scanning screen" src="https://github.com/user-attachments/assets/1e99f554-bd40-4a65-979c-31d309636393" />

### Opened device info
<img width="1080" height="2106" alt="BLEEX opened device information" src="https://github.com/user-attachments/assets/f5442f88-e033-4bf8-8ff2-97cf0184ceb3" />







## Tech Stack

- Kotlin
- Jetpack Compose
- Android ViewModel
- Kotlin Coroutines
- Kotlin Flow / `callbackFlow`
- Android Bluetooth Low Energy APIs
- JUnit / Coroutine Test
- GitHub Actions
- Android Lint
- Trivy

## Project Structure
```text
com.example.bleex
├── App.kt
├── MainActivity.kt
│
├── bluetooth
│   ├── AndroidBleScanner.kt
│   ├── BleDevice.kt
│   ├── BlePermissions.kt
│   └── BleScanner.kt
│
├── ui
│   ├── ScanScreen.kt
│   ├── StartScreen.kt
│   ├── components
│   └── theme
│
└── viewmodel
    └── ScanViewModel.kt
```

## Testing
The project contains unit tests for the ScanViewModel, including:

- Adding newly discovered devices
- Updating previously discovered devices
- Starting and stopping scans
- Preventing duplicate scans
- Handling Bluetooth being disabled
- Reacting to Bluetooth being disabled during an active scan

The scanner is abstracted behind the BleScanner interface, allowing the ViewModel to be tested without requiring a physical Bluetooth device.


## Future Improvements

- Improve BLE advertisement and manufacturer data decoding
- Add filtering and sorting of discovered devices
- Add more comprehensive UI/instrumentation tests
- Add scan timeout and more advanced scan configuration


    

