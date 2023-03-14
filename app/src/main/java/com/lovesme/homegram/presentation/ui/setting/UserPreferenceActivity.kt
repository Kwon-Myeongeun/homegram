package com.lovesme.homegram.presentation.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import com.lovesme.homegram.databinding.ActivityUserpreferenceBinding
import com.lovesme.homegram.presentation.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserPreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserpreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserpreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                UserPreferencesRepository().updateUser(
                    binding.nameSettingTv.text.toString(),
                    binding.birthSettingTv.text.toString()
                )
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}