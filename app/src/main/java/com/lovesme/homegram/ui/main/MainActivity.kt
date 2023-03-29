package com.lovesme.homegram.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.lovesme.homegram.databinding.ActivityMainBinding
import com.lovesme.homegram.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController =
            supportFragmentManager.findFragmentById(binding.homeFrmContainer.id)
                ?.findNavController()
        navController?.let { binding.homeBtmNavigation.setupWithNavController(it) }

        binding.homeToolbar.setOnMenuItemClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
            return@setOnMenuItemClickListener true
        }
    }
}