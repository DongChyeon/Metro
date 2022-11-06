package com.dongchyeon.metro.util

import android.Manifest.permission.*

class Constants {
    companion object {
        const val REQUEST_PERMISSION = 1
        val PERMISSIONS = arrayOf(
            ACCESS_FINE_LOCATION,
            BLUETOOTH_CONNECT,
            BLUETOOTH_SCAN
        )

        // 사용자 BLE UUID Service/Rx/Tx
        const val SERVICE_STRING = "0000ffe0-0000-1000-8000-00805f9b34fb"
        const val CHARACTERISTIC_COMMAND_STRING = "0000ffe1-0000-1000-8000-00805f9b34fb"
        const val CHARACTERISTIC_RESPONSE_STRING = "0000ffe1-0000-1000-8000-00805f9b34fb"
        const val MAC_ADDR = "78:04:73:B8:4F:B9"

        // BluetoothGattDescriptor 고정
        const val CLIENT_CHARACTERISTIC_CONFIG = "0000ffe1-0000-1000-8000-00805f9b34fb"
    }
}