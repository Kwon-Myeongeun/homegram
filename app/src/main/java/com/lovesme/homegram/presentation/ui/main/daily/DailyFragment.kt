package com.lovesme.homegram.presentation.ui.main.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lovesme.homegram.databinding.FragmentDailyBinding
import com.lovesme.homegram.presentation.ui.viewmodel.DailyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyFragment : Fragment() {

    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private val dailyViewModel: DailyViewModel by activityViewModels()
    private val adapter = DailyQuestionRVAdapter()

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
}