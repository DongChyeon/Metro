package com.dongchyeon.metro.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dongchyeon.metro.R
import com.dongchyeon.metro.databinding.ActivityNfcBinding

class NfcActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNfcBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityNfcBinding>(this, R.layout.activity_nfc)
    }
}