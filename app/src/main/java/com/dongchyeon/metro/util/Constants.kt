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
        const val SERVICE_STRING = "0000183B-0000-1000-8000-00805F9B34FB"
        const val CHARACTERISTIC_COMMAND_STRING = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E"
        const val CHARACTERISTIC_RESPONSE_STRING = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E"
        const val MAC_ADDR = "78:04:73:B6:55:92"

        // BluetoothGattDescriptor 고정
        const val CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb"
    }
}