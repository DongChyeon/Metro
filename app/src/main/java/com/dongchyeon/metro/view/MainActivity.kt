package com.dongchyeon.metro.view

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongchyeon.metro.App.Companion.bluetoothAdapter
import com.dongchyeon.metro.ConnectThread
import com.dongchyeon.metro.Constants.Companion.REQUEST_ALL_PERMISSION
import com.dongchyeon.metro.adapter.BtAdapter
import com.dongchyeon.metro.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val registerForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(applicationContext, "블루투스 연결 완료", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "블루투스 연결 실패", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        grantPermissions()
        checkBluetooth()
        enableBluetooth()

        val adapter = BtAdapter()
        binding.btRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.btRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : BtAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                binding.constraintLayout.visibility = View.GONE

                val address = adapter.getDevice(position).second
                val device = bluetoothAdapter.getRemoteDevice(address)

                val connectThread = ConnectThread(device)
                connectThread.start()
            }
        })

        binding.btBtn.setOnClickListener {
            binding.constraintLayout.visibility = View.VISIBLE

            val pairedDevices = ArrayList<Pair<String, String>>()
            for (device in bluetoothAdapter.bondedDevices) {
                pairedDevices.add(Pair(device.name, device.address))
            }

            adapter.submitList(pairedDevices)
        }

        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding.stnNmEdit.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                val intent = Intent(applicationContext, SubwayInfoActivity::class.java)
                intent.putExtra("statnNm", binding.stnNmEdit.text.toString())
                startActivity(intent)
                true
            } else {
                false
            }
        }

        binding.searchBtn.setOnClickListener {
            val intent = Intent(applicationContext, SubwayInfoActivity::class.java)
            intent.putExtra("statnNm", binding.stnNmEdit.text.toString())
            startActivity(intent)
        }
    }

    private fun checkBluetooth() {
        fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)

        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH) }?.also {
            Toast.makeText(this, "bluetooth가 지원 되지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }
            ?.also {
                Toast.makeText(this, "ble가 지원 되지 않습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun grantPermissions() {
        val permissions = arrayOf(
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION,
            BLUETOOTH_CONNECT
        )

        requestPermissions(permissions, REQUEST_ALL_PERMISSION)
    }

    private fun enableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            registerForResult.launch(enableBtIntent)
        }
    }
}