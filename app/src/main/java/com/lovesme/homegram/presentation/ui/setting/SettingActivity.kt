package com.lovesme.homegram.presentation.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lovesme.homegram.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}