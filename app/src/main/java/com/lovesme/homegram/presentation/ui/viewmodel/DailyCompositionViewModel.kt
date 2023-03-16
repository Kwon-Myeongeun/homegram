package com.lovesme.homegram.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyCompositionViewModel @Inject constructor(private val repository: QuestionRepository) :
    ViewModel() {

    fun updateAnswer(groupId: String, seq: String) {
        viewModelScope.launch {
            val result = repository.updateAnswer(groupId, seq)
        }
    }
}