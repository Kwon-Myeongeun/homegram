package com.lovesme.homegram.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import com.lovesme.homegram.databinding.ActivityUserpreferenceBinding
import com.lovesme.homegram.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserPreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserpreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserpreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveBtn.setOnClickListener {
            val name = binding.nameSettingTv.text
            val birth = binding.birthSettingTv.text
            CoroutineScope(Dispatchers.IO).launch {
                val deferredGroupId = async {
                    UserPreferencesRepository().getGroupId()
                }
                val result = deferredGroupId.await()
                if (result is Result.Success && result.data != null) {
                    UserPreferencesRepository().updateUser(
                        result.data.toString(),
                        name.toString(),
                        birth.toString()
                    )
                } else {
                    null
                }
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}