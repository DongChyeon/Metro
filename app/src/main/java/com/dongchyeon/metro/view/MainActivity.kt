package com.dongchyeon.metro.view

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongchyeon.metro.R
import com.dongchyeon.metro.adapter.BleAdapter
import com.dongchyeon.metro.databinding.ActivityMainBinding
import com.dongchyeon.metro.util.Constants.Companion.PERMISSIONS
import com.dongchyeon.metro.util.Constants.Companion.REQUEST_PERMISSION
import com.dongchyeon.metro.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bleAdapter: BleAdapter
    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.viewModel = viewModel

        bleAdapter = BleAdapter()
        binding.btRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.btRecyclerView.adapter = bleAdapter
        bleAdapter.setOnItemClickListener(object : BleAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                binding.constraintLayout.visibility = View.GONE
                viewModel.connectDevice(bleAdapter.getDevice(position))
            }
        })

        // 필요 권한 체크 후 허용
        requirePermissions(applicationContext, PERMISSIONS)

        binding.btBtn.setOnClickListener {
            binding.constraintLayout.visibility = View.VISIBLE
            viewModel.onClickScan()
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

        binding.nfcBtn.setOnClickListener {
            startActivity(Intent(applicationContext, NfcActivity::class.java))
        }

        initObserver(binding)
    }

    private fun initObserver(binding: ActivityMainBinding) {
        viewModel.requestEnableBLE.observe(this) {
            it.getContentIfNotHandled()?.let {
                requestEnableBLE()
            }
        }
        viewModel.listUpdate.observe(this) {
            it.getContentIfNotHandled()?.let { scanResults ->
                bleAdapter.submitList(scanResults)
            }
        }

        viewModel._isScanning.observe(this) {
            it.getContentIfNotHandled()?.let { scanning ->
                viewModel.isScanning.set(scanning)
            }
        }
        viewModel._isConnect.observe(this) {
            it.getContentIfNotHandled()?.let { connect ->
                viewModel.isConnect.set(connect)
            }
        }
        viewModel.statusTxt.observe(this) {
            binding.statusText.text = it
        }

        viewModel.readTxt.observe(this) {
            Log.d("read", it)
        }
    }

    override fun onResume() {
        super.onResume()
        // finish app if the BLE is not supported
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish()
        }
    }

    private val requestEnableBleResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                // do somthing after enableBleRequest
            }
        }

    /**
     * Request BLE enable
     */
    private fun requestEnableBLE() {
        val bleEnableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestEnableBleResult.launch(bleEnableIntent)
    }

    private fun requirePermissions(context: Context?, permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(permission), // 1
                        REQUEST_PERMISSION
                    )
                }
            }
        }
    }
}