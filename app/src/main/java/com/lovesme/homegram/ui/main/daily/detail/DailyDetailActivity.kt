package com.lovesme.homegram.ui.main.daily.detail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.databinding.ActivityDailyDetailBinding
import com.lovesme.homegram.ui.main.daily.composition.DailyCompositionActivity
import com.lovesme.homegram.ui.viewmodel.DailyDetailViewModel
import com.lovesme.homegram.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DailyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDailyDetailBinding
    private val adapter = DailyDetailRVAdapter()

    private val dailyDetailViewModel: DailyDetailViewModel by viewModels()

    private lateinit var dailyCompositionResultLauncher: ActivityResultLauncher<Intent>

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
        dailyDetailViewModel.setAnswer(item?.answer)

        lifecycleScope.launch {
            dailyDetailViewModel.answer
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { answer ->
                    item?.answer = answer.toMutableList()
                    adapter.submitList(answer.toMutableList())
                }
        }

        dailyCompositionResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val contents = result.data?.getStringExtra(Constants.PARCELABLE_ANSWER_TEXT)
                if (contents != null) {
                    item?.let {
                        dailyDetailViewModel.updateAnswer(item.no, it.key, contents)
                    }
                }
            }
        }

        binding.dailyDetailToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.writeAnswerBtn.setOnClickListener {
            lifecycleScope.launch {
                val intent = Intent(this@DailyDetailActivity, DailyCompositionActivity::class.java)
                val answer = dailyDetailViewModel.getMyAnswer(item)
                answer?.let {
                    intent.putExtra(Constants.PARCELABLE_ANSWER_TEXT, it)
                }
                item?.let {
                    intent.putExtra(Constants.PARCELABLE_CONTENT, it.contents)
                }
                dailyCompositionResultLauncher.launch(intent)
            }
        }
    }
}