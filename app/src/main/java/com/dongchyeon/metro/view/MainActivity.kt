package com.dongchyeon.metro.view

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dongchyeon.metro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
}