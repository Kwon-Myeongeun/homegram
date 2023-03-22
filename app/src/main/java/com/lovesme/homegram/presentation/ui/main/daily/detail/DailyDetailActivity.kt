package com.lovesme.homegram.presentation.ui.main.daily.detail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.databinding.ActivityDailyDetailBinding
import com.lovesme.homegram.presentation.ui.main.daily.composition.DailyCompositionActivity
import com.lovesme.homegram.presentation.ui.viewmodel.DailyDetailViewModel
import com.lovesme.homegram.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDailyDetailBinding
    private val adapter = DailyDetailRVAdapter()

    private val dailyDetailViewModel: DailyDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.PARCELABLE_QUESTION, Question::class.java)
        } else {
            intent.getParcelableExtra<Question>(Constants.PARCELABLE_QUESTION)
        }
        binding.dailyDetailRecycler.adapter = adapter
        binding.dailyDetailTitleTv.text = item?.contents
        adapter.submitList(item?.answer?.toMutableList())

        binding.dailyDetailToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.dailyDetailToolbar.setOnMenuItemClickListener {
            val intent = Intent(this, DailyCompositionActivity::class.java)
            intent.putExtra(Constants.PARCELABLE_QUESTION, item)
            startActivity(intent)
            return@setOnMenuItemClickListener true
        }
    }
}