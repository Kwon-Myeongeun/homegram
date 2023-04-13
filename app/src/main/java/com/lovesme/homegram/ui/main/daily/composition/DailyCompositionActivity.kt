package com.lovesme.homegram.ui.main.daily.composition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lovesme.homegram.databinding.ActivityDailyCompositionBinding
import com.lovesme.homegram.ui.main.daily.detail.DailyDetailActivity
import com.lovesme.homegram.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyCompositionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDailyCompositionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyCompositionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getStringExtra(Constants.PARCELABLE_ANSWER_TEXT)
        val content = intent.getStringExtra(Constants.PARCELABLE_CONTENT)
        item?.let {
            binding.dailyCompositionContentTv.setText(it)
        }
        content?.let {
            binding.dailyCompositionTitleTv.text = it
        }

        binding.dailyCompositionToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.dailyCompositionToolbar.setOnMenuItemClickListener {
            val answer = binding.dailyCompositionContentTv.text
            val intent = Intent(this, DailyDetailActivity::class.java).apply {
                if (!answer.isNullOrEmpty()) {
                    putExtra(Constants.PARCELABLE_ANSWER_TEXT, answer.toString())
                }
            }
            setResult(RESULT_OK, intent)
            finish()
            return@setOnMenuItemClickListener true
        }
    }
}