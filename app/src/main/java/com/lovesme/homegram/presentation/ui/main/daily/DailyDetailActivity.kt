package com.lovesme.homegram.presentation.ui.main.daily

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lovesme.homegram.databinding.ActivityDailyDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDailyDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}