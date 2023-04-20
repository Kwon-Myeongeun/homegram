package com.lovesme.homegram.ui.main.daily

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.lovesme.homegram.R
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.listener.QuestionClickListener
import com.lovesme.homegram.databinding.FragmentDailyBinding
import com.lovesme.homegram.ui.main.daily.detail.DailyDetailActivity
import com.lovesme.homegram.ui.viewmodel.DailyViewModel
import com.lovesme.homegram.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DailyFragment : Fragment(), QuestionClickListener {

    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private val dailyViewModel: DailyViewModel by activityViewModels()
    private val adapter = DailyQuestionRVAdapter(this)
    var isFirst = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dailyTabRecycler.adapter = adapter
        dailyViewModel.loadQuestion()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    dailyViewModel.questions.collect { question ->
                        adapter.submitList(question.toMutableList())
                        arguments?.getString(Constants.PARCELABLE_NO)?.let { uploadNo ->
                            if (question.isNotEmpty()) {
                                if (isFirst) {
                                    val uploadQuestion = question.first { it.no == uploadNo }
                                    val intent = Intent(context, DailyDetailActivity::class.java)
                                    intent.putExtra(Constants.PARCELABLE_QUESTION, uploadQuestion)
                                    startActivity(intent)

                                    isFirst = false
                                }
                            }
                        }
                    }
                }
                launch {
                    dailyViewModel.connect.collect { connect ->
                        if (!connect) {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.internet_connect_fail),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

        }
    }

    override fun onClickItem(question: Question) {
        val intent = Intent(context, DailyDetailActivity::class.java)
        intent.putExtra(Constants.PARCELABLE_QUESTION, question)
        startActivity(intent)
    }
}