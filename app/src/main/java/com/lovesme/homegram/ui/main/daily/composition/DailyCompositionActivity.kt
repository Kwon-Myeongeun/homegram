package com.lovesme.homegram.ui.main.daily.composition

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.databinding.ActivityDailyCompositionBinding
import com.lovesme.homegram.ui.viewmodel.DailyCompositionViewModel
import com.lovesme.homegram.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyCompositionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDailyCompositionBinding

    private val dailyCompositionViewModel: DailyCompositionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyCompositionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.PARCELABLE_QUESTION, Question::class.java)
        } else {
            intent.getParcelableExtra<Question>(Constants.PARCELABLE_QUESTION)
        }

        binding.dailyCompositionToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.dailyCompositionToolbar.setOnMenuItemClickListener {
            val answer = binding.dailyCompositionContentTv.text
            item?.let {
                dailyCompositionViewModel.updateAnswer(it.seq, answer.toString())
            }
            finish()
            return@setOnMenuItemClickListener true
        }
    }
}