package com.lovesme.homegram.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lovesme.homegram.databinding.ActivityUserpreferenceBinding
import com.lovesme.homegram.ui.main.MainActivity
import com.lovesme.homegram.ui.viewmodel.UserPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserpreferenceBinding
    private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserpreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveBtn.setOnClickListener {
            userPreferenceViewModel.updateUserInfo(
                binding.nameSettingTv.text.toString(),
                binding.birthSettingTv.text.toString()
            )
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}