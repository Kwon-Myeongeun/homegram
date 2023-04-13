package com.lovesme.homegram.ui.setting

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.lovesme.homegram.R
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.databinding.ActivityUserpreferenceBinding
import com.lovesme.homegram.ui.main.MainActivity
import com.lovesme.homegram.ui.viewmodel.UserPreferenceViewModel
import com.lovesme.homegram.util.DateFormatText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
            lifecycleScope.launch {
                val result = userPreferenceViewModel.updateUserInfo(
                    binding.nameSettingTv.text.toString(),
                    binding.birthSettingTv.text.toString()
                )
                if (result is Result.Success) {
                    startActivity(Intent(this@UserPreferenceActivity, MainActivity::class.java))
                    finish()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.download_data_fail),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        binding.nameSettingTv.doAfterTextChanged {
            binding.saveBtn.isEnabled =
                !it.isNullOrEmpty() && !binding.birthSettingTv.text.isNullOrEmpty()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = dateFormatText.convertToDateText(year, month, dayOfMonth)
        binding.birthSettingTv.setText(date)
        if (!binding.nameSettingTv.text.isNullOrEmpty()) {
            binding.saveBtn.isEnabled = true
        }
    }
}