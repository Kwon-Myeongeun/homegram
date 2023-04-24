package com.lovesme.homegram.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.lovesme.homegram.R
import com.lovesme.homegram.databinding.FragmentHomeBinding
import com.lovesme.homegram.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            if (homeViewModel.notExistMember()) {
                binding.homeTv.text = "우측 상단의 설정 아이콘 → [초대 링크 공유하기]를 통해 멤버를 초대해 보세요."
            } else if (homeViewModel.isNotAnswered()) {
                binding.homeTv.text = "최근 질문에 답변을 하지 않으면 질문이 갱신되지 않아요."
                binding.homeIv.setImageResource(R.drawable.pet_hungry_origin)
            }
        }
    }
}