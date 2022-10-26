package com.dongchyeon.metro.view

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dongchyeon.metro.R
import com.dongchyeon.metro.adapter.LineAdapter
import com.dongchyeon.metro.databinding.ActivitySubwayInfoBinding
import com.dongchyeon.metro.viewmodel.SubwayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubwayInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubwayInfoBinding
    private val subwayViewModel: SubwayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subway_info)
        binding.apply {
            lifecycleOwner = this@SubwayInfoActivity
            viewModel = subwayViewModel
        }

        val statnNm = intent.getStringExtra("statnNm") ?: ""
        binding.stnNmEdit.setText(statnNm)
        binding.stnNmEdit.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                subwayViewModel.loadData(binding.stnNmEdit.text.toString())
                true
            } else {
                false
            }
        }
        subwayViewModel.loadData(statnNm)

        val adapter = LineAdapter(applicationContext)
        binding.pager.adapter = adapter

        subwayViewModel.getData().observe(this) { data ->
            data.let { adapter.submitList(data) }
        }
    }
}