package com.dongchyeon.metro.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
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
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.stnNmEdit.windowToken, 0)
                true
            } else {
                false
            }
        }
        binding.searchBtn.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.stnNmEdit.windowToken, 0)
            subwayViewModel.loadData(binding.stnNmEdit.text.toString())
        }

        subwayViewModel.loadData(statnNm)

        val adapter = LineAdapter(applicationContext)
        binding.pager.adapter = adapter

        subwayViewModel.subwayData.observe(this) { data ->
            data.let { adapter.submitList(data) }
        }
    }
}