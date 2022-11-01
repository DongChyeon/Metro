package com.dongchyeon.metro

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.SystemClock
import android.util.Log
import com.dongchyeon.metro.Constants.Companion.BT_UUID
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("MissingPermission")
class ConnectThread(private val device: BluetoothDevice) : Thread() {
    private val socket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        device.createRfcommSocketToServiceRecord(BT_UUID)
    }

    private var inputStream: InputStream? = null
    private var outStream: OutputStream? = null

    override fun run() {
        socket?.let { socket ->
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            socket.connect()

            inputStream = socket.inputStream
            outStream = socket.outputStream
        }

        var buffer: ByteArray // buffer store for the stream
        var bytes: Int // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = inputStream!!.available()
                if (bytes != 0) {
                    buffer = ByteArray(1024)
                    SystemClock.sleep(100) //pause and wait for rest of data. Adjust this depending on your sending speed.
                    bytes = inputStream!!.available() // how many bytes are ready to be read?
                    bytes =
                        inputStream!!.read(
                            buffer,
                            0,
                            bytes
                        ) // record how many bytes we actually read

                    Log.d("read", bytes.toString())
                }
            } catch (e: IOException) {
                e.printStackTrace()
                break
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    fun write(input: String) {
        val bytes = input.toByteArray() //converts entered String into bytes
        try {
            outStream!!.write(bytes)
        } catch (e: IOException) {
            Log.d("error", e.toString())
        }
    }

    /* Call this from the main activity to shutdown the connection */
    fun cancel() {
        try {
            socket?.close()
        } catch (e: IOException) {
            Log.d("error", e.toString())
        }
    }
}