package com.dongchyeon.metro

import android.Manifest.permission.*
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class Constants {
    companion object {
        const val REQUEST_ENABLE_BT = 1     // used to identify adding bluetooth names
        const val MESSAGE_READ = 2      // used in bluetooth handler to identify message update
        const val CONNECTING_STATUS = 3     // used in bluetooth handler to identify message status
        const val REQUEST_ALL_PERMISSION = 4    // used to request fine location permission
        val BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        @RequiresApi(Build.VERSION_CODES.S)
        val PERMISSIONS = arrayOf(
            BLUETOOTH,
            BLUETOOTH_ADMIN,
            BLUETOOTH_ADVERTISE,
            BLUETOOTH_CONNECT,
            BLUETOOTH_SCAN,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )
    }
}