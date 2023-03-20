package com.lovesme.homegram.ui.main.daily

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.listener.QuestionClickListener
import com.lovesme.homegram.databinding.FragmentDailyBinding
import com.lovesme.homegram.ui.main.daily.detail.DailyDetailActivity
import com.lovesme.homegram.ui.viewmodel.DailyViewModel
import com.lovesme.homegram.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyFragment : Fragment(), QuestionClickListener {

    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private val dailyViewModel: DailyViewModel by activityViewModels()
    private val adapter = DailyQuestionRVAdapter(this)

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
        dailyViewModel.questions.observe(viewLifecycleOwner) { question ->
            adapter.submitList(question.toMutableList())
        }
    }

    override fun onClickItem(question: Question) {
        val intent = Intent(context, DailyDetailActivity::class.java)
        intent.putExtra(Constants.PARCELABLE_QUESTION, question)
        startActivity(intent)
    }
}