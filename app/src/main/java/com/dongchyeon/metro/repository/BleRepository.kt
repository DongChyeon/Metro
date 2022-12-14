package com.dongchyeon.metro.repository

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.os.ParcelUuid
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dongchyeon.metro.util.BluetoothUtils
import com.dongchyeon.metro.util.Constants.Companion.CLIENT_CHARACTERISTIC_CONFIG
import com.dongchyeon.metro.util.Constants.Companion.SERVICE_STRING
import com.dongchyeon.metro.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import kotlin.concurrent.schedule

@SuppressLint("MissingPermission")
class BleRepository(private val context: Context) {
    private val TAG = "Central"

    private val bleManager: BluetoothManager =
        context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
    val bleAdapter: BluetoothAdapter?
        get() = bleManager.adapter
    private var bleGatt1: BluetoothGatt? = null
    private var bleGatt2: BluetoothGatt? = null

    var scanResults: ArrayList<BluetoothDevice>? = ArrayList()

    var statusTxt: String = ""
    var txtRead1: String = ""
    var txtRead2: String = ""

    var isStatusChange: Boolean = false
    var isTxtRead1: Boolean = false
    var isTxtRead2: Boolean = false

    fun fetchReadText1() = flow {
        while (true) {
            if (isTxtRead1) {
                emit(txtRead1)
                isTxtRead1 = false
            }
        }
    }.flowOn(Dispatchers.Default)

    fun fetchReadText2() = flow {
        while (true) {
            if (isTxtRead2) {
                emit(txtRead2)
                isTxtRead2 = false
            }
        }
    }.flowOn(Dispatchers.Default)

    fun fetchStatusText() = flow {
        while (true) {
            if (isStatusChange) {
                emit(statusTxt)
                isStatusChange = false
            }
        }
    }.flowOn(Dispatchers.Default)

    val requestEnableBLE = MutableLiveData<Event<Boolean>>()
    val isScanning = MutableLiveData(Event(false))
    val isConnect = MutableLiveData(Event(false))
    val listUpdate = MutableLiveData<Event<ArrayList<BluetoothDevice>?>>()

    fun startScan() {
        // check ble adapter and ble enabled
        if (bleAdapter == null || !bleAdapter?.isEnabled!!) {
            requestEnableBLE.postValue(Event(true))
            statusTxt = "?????? ?????? : BLE ??? ???????????? ???????????????."
            isStatusChange = true
            return
        }

        // scan filter
        val filters: MutableList<ScanFilter> = ArrayList()
        val scanFilter: ScanFilter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(UUID.fromString(SERVICE_STRING)))
            //.setDeviceAddress(MAC_ADDR)
            .build()
        filters.add(scanFilter)

        // scan settings
        // set low power scan mode
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()

        // start scan
        bleAdapter?.bluetoothLeScanner?.startScan(filters, settings, bleScanCallback)
        //bleAdapter?.bluetoothLeScanner?.startScan(bleScanCallback)

        statusTxt = "?????? ???...."
        isStatusChange = true
        isScanning.postValue(Event(true))

        Timer("SettingUp", false).schedule(3000) { stopScan() }
    }

    private fun stopScan() {
        bleAdapter?.bluetoothLeScanner?.stopScan(bleScanCallback)
        isScanning.postValue(Event(false))
        statusTxt = "?????? ??????. ????????? ????????? ????????? ???????????????."
        isStatusChange = true

        scanResults = ArrayList() //list ?????????
        Log.d(TAG, "BLE Stop!")
    }

    /**
     * BLE Scan Callback
     */
    private val bleScanCallback: ScanCallback =
        object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                Log.i(TAG, "Remote device name: " + result.device.name)
                Log.i(TAG, "Remote device address: " + result.device.address)
                addScanResult(result)
            }

            override fun onBatchScanResults(results: List<ScanResult>) {
                for (result in results) {
                    addScanResult(result)
                }
            }

            override fun onScanFailed(_error: Int) {
                Log.e(TAG, "BLE scan failed with code $_error")
                statusTxt = "BLE ?????? ?????? (?????? ?????? : $_error)"
                isStatusChange = true
            }

            /**
             * Add scan result
             */
            private fun addScanResult(result: ScanResult) {
                // get scanned device
                val device = result.device
                // get scanned device MAC address
                val deviceAddress = device.address
                val deviceName = device.name
                // add the device to the result list
                for (dev in scanResults!!) {
                    if (dev.address == deviceAddress) return
                }
                scanResults?.add(result.device)
                // log
                statusTxt = "????????? ?????? ?????? : $deviceAddress"
                isStatusChange = true
                listUpdate.postValue(Event(scanResults))
            }
        }

    /**
     * BLE gattClientCallback
     */
    private val gattClientCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (status == BluetoothGatt.GATT_FAILURE) {
                disconnectGattServer(gatt)
                return
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                disconnectGattServer(gatt)
                return
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // update the connection status message
                statusTxt = if (bleGatt2 == null) {
                    "???????????? ?????? ????????? ?????????????????????."
                } else {
                    "???????????? ????????? ?????? ?????????????????????."
                }
                isStatusChange = true
                Log.d(TAG, "Connected to the GATT server")
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                disconnectGattServer(gatt)
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            // check if the discovery failed
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "Device service discovery failed, status: $status")
                return
            }
            // log for successful discovery
            Log.d(TAG, "Services discovery is successful")
            isConnect.postValue(Event(true))

            gatt!!.services.forEach { gattService ->
                Log.d("service", gattService.toString() + " / " + gattService.uuid.toString())
                for (characteristic in gattService.characteristics) {
                    Log.d(
                        "characteristic",
                        characteristic.toString() + " / " + characteristic.uuid.toString()
                    )
                }
            }

            // find command characteristics from the GATT server
            val respCharacteristic = gatt?.let { BluetoothUtils.findResponseCharacteristic(it) }
            // disconnect if the characteristic is not found
            if (respCharacteristic == null) {
                Log.e(TAG, "Unable to find cmd characteristic")
                disconnectGattServer(gatt)
                return
            }
            gatt.setCharacteristicNotification(respCharacteristic, true)

            // UUID for notification
            val descriptor: BluetoothGattDescriptor = respCharacteristic!!.getDescriptor(
                UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG)
            )
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            Log.d(TAG, "characteristic changed: " + characteristic.uuid.toString())
            readCharacteristic(gatt, characteristic)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Characteristic written successfully")
            } else {
                Log.e(TAG, "Characteristic write unsuccessful, status: $status")
                disconnectGattServer(gatt)
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Characteristic read successfully")
                readCharacteristic(gatt, characteristic)
            } else {
                Log.e(TAG, "Characteristic read unsuccessful, status: $status")
                // Trying to read from the Time Characteristic? It doesnt have the property or permissions
                // set to allow this. Normally this would be an error and you would want to:
                // disconnectGattServer()
            }
        }

        /**
         * Log the value of the characteristic
         * @param characteristic
         */

        private fun readCharacteristic(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            val msg = characteristic.getStringValue(0)

            if (gatt == bleGatt1) {
                txtRead1 = msg
                isTxtRead1 = true
            } else if (gatt == bleGatt2) {
                txtRead2 = msg
                isTxtRead2 = true
            }
        }
    }

    fun getPressure(people: String): Int {
        return if (people == "pregnant") {
            txtRead1.toInt()
        } else {
            txtRead2.toInt()
        }
    }

    fun isConnected(): Boolean {
        return bleGatt1 != null && bleGatt2 != null
    }

    /**
     * Connect to the ble device
     */
    fun connectDevice(device: BluetoothDevice?) {
        // update the status
        statusTxt = "?????? ??? ${device?.address}"
        isStatusChange = true
        if (bleGatt1 == null) {
            bleGatt1 = device?.connectGatt(context, false, gattClientCallback)
        } else {
            bleGatt2 = device?.connectGatt(context, false, gattClientCallback)
        }

        scanResults?.remove(device!!)
    }

    /**
     * Disconnect Gatt Server
     */
    fun disconnectGattServer(gatt: BluetoothGatt?) {
        Log.d(TAG, "Closing Gatt connection")
        // disconnect and close the gatt
        if (gatt != null) {
            gatt.disconnect()
            gatt.close()
            statusTxt = "????????? ??????????????????."
            isStatusChange = true
            isConnect.postValue(Event(false))
        }
    }

    /*
    fun writeData(cmdByteArray: ByteArray) {
        val cmdCharacteristic = BluetoothUtils.findCommandCharacteristic(bleGatt1!!)
        // disconnect if the characteristic is not found
        if (cmdCharacteristic == null) {
            Log.e(TAG, "Unable to find cmd characteristic")
            disconnectGattServer()
            return
        }

        cmdCharacteristic.value = cmdByteArray
        val success: Boolean = bleGatt1!!.writeCharacteristic(cmdCharacteristic)
        // check the result
        if (!success) {
            Log.e(TAG, "Failed to write command")
        }
    }
     */
}
