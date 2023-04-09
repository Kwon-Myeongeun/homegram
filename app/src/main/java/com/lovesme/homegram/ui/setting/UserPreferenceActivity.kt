package com.lovesme.homegram.ui.setting

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.lovesme.homegram.databinding.ActivityUserpreferenceBinding
import com.lovesme.homegram.ui.main.MainActivity
import com.lovesme.homegram.ui.viewmodel.UserPreferenceViewModel
import com.lovesme.homegram.util.DateFormatText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserPreferenceActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityUserpreferenceBinding
    private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()

    @Inject
    lateinit var dateFormatText: DateFormatText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserpreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.birthSettingTv.setOnClickListener {
            DatePickerDialog(
                this,
                this,
                dateFormatText.getTodayYEAR(),
                dateFormatText.getTodayMONTH(),
                dateFormatText.getTodayDATE(),
            ).show()
        }

        binding.saveBtn.setOnClickListener {
            userPreferenceViewModel.updateUserInfo(
                binding.nameSettingTv.text.toString(),
                binding.birthSettingTv.text.toString()
            )
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.nameSettingTv.doAfterTextChanged {
            binding.saveBtn.isEnabled = !it.isNullOrEmpty() && !binding.birthSettingTv.text.isNullOrEmpty()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.birthSettingTv.setText("${year}.${month}.${dayOfMonth}")
        if (!binding.nameSettingTv.text.isNullOrEmpty()) {
            binding.saveBtn.isEnabled = true
        }
    }
}